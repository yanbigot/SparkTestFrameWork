package api

import api.ODataClient.getDate
import org.apache.http.client.methods.{HttpGet, HttpPost}
import org.apache.http.impl.client.DefaultHttpClient

import scala.io.Source
import scala.xml.XML

object ApacheHttpClient {

  val X_CSOD_API_KEY = "x-csod-api-key"
  val X_CSOD_SESSION_TOKEN = "x-csod-session-token"
  val X_CSOD_DATE = "x-csod-date"
  val X_CSOD_SIGNATURE = "x-csod-signature"
  val CONTENT_TYPE = "Content-Type"
  val ACCEPT = "Accept"
  val TEXT_XML = "text/xml"

  def main(args: Array[String]) {
    val map = authenticate()
    if(map.getOrElse("token", "").length > 0 ){
      //view: String, token: String, alias: String, secret: String
      getRequestSignature("vw_rpt_training", map.getOrElse("token", ""), map.getOrElse("alias",""), map.getOrElse("secret",""))
    }
    else{
      println("AUTHENTICATION FAILED !!! ")
    }
  }

  private def logParams( userName: String, apiId: String, apiSecret: String, date: String, signature: String ) = {
    println("date: " + date)
    println("signature: " + signature)
    println("apiId: " + apiId)
    println("apiSecret: " + apiSecret)
    println("userName: " + userName)
  }

  private def logRequest( post: HttpPost ) = {
    println("--- REQUEST METHOD---")
    println(post.getMethod())
    println("--- REQUEST HEADERS---")
    post.getAllHeaders.foreach(arg => println(arg))
    println("--- REQUEST getProtocolVersion---")
    println(post.getProtocolVersion)
    println("--- REQUEST getRequestLine---")
    println(post.getRequestLine)
  }

  def authenticate(): Map[String, String] ={
    val url = "https://socgen-stg.csod.com"
    val sessionUrl = "/services/api/sts/session"
    val userName = ApiCode.getProperty(ApiCode.USER_NAME)
    val httpMethod = "POST"
    val userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.52 Safari/537.17')"
    val apiId = ApiCode.getProperty(ApiCode.API_ID)
    val apiSecret = ApiCode.getProperty(ApiCode.API_SECRET)
    val keyName = "x-csod-api-key"
    val date = getDate
    val stringToSign = HMACgen.buildStringToSign(date,httpMethod, keyName, apiId, sessionUrl)

    val signature = HMACgen.generateSignature(apiSecret, stringToSign, date, "", "HmacSHA512")

    val post: HttpPost = new HttpPost(url + sessionUrl + "?userName=" + userName + "&alias=" + date)
    post.addHeader(X_CSOD_DATE , date)
    post.addHeader(X_CSOD_SIGNATURE , signature)
    post.addHeader(X_CSOD_API_KEY , apiId )
    post.addHeader(CONTENT_TYPE , TEXT_XML)
    post.addHeader(ACCEPT , TEXT_XML)
    post.expectContinue()

    val httpClient = new DefaultHttpClient()
    val httpResponse = httpClient.execute(post)
    httpResponse.getAllHeaders.foreach(arg => println(arg))
    val entity = httpResponse.getEntity()
    var content = ""
    if (entity != null) {
      val inputStream = entity.getContent()
      content = Source.fromInputStream(inputStream).getLines.mkString("\n")
      inputStream.close
    }

    httpClient.getConnectionManager().shutdown()
    println("----------------------------------------------------------------------")
    println(content)
    parseAutenticationResponse(content)
  }

  /**
    * <cornerstoneApi xmlns:i="http://www.w3.org/2001/XMLSchema-instance">
    *   <Validations/>
    *   <status>201</status>
    *   <timeStamp>2017-11-27T19:50:32+0000</timeStamp>
    *   <createdRecords>1</createdRecords>
    *   <data xmlns:a="www.CornerStoneOnDemand.com/Core/">
    *     <a:Session>
    *       <a:Token>4x5zyx0dyfvn</a:Token>
    *       <a:Secret>UQJou4BN7LLgNR78DQLay3vtupBguPuqXfp44qeUi20H3OpiYqzir9ZnUMkvxZUmSNPM9jKRNQ7BwO36Aa3Q3g==</a:Secret>
    *       <a:Alias>2017-11-27T19:50:30.000</a:Alias>
    *       <a:ExpiresOn>2017-11-27T20:50:32+0000</a:ExpiresOn>
    *     </a:Session>
    *   </data>
    * </cornerstoneApi>
    */
  def parseAutenticationResponse(content: String): Map[String, String] ={
    val xml = XML.loadString(content)
    val status  = (xml \\ "status"  ) text
    val token  = (xml \\ "Token" ) text
    val secret = (xml \\ "Secret") text
    val alias = (xml \\ "Alias") text

    println("status " + status)
    println("token " + token)
    println("secret " + secret)
    println("alias " + alias)

    Map(
      "token" -> token,
      "secret" -> secret,
      "alias" -> alias
    )
  }

  /**
    *
    *
    $httpMethod = 'GET';
    $key = 'x-csod-session-token:'.$token;
    $httpUrl = $url;
    date_default_timezone_set('UTC');
    $date = 'x-csod-date:'.date('Y-m-d').'T'.date('H:i:s').'.000';
    $stringToSign = $httpMethod."\n".$date."\n".$key."\n".$httpUrl;
    //Generate the signature

    $secretKey = base64_decode($secret);

    $signature = base64_encode(hash_hmac('sha512', $stringToSign, $secretKey,true));
    */
  def getRequestSignature(view: String, token: String, alias: String, secret: String): Unit = {
    val keyName = "x-csod-session-token"
    val date = getDate
    //$stringToSign = $httpMethod."\n".$date."\n".$key."\n".$httpUrl;
    val stringToSign = "GET\nx-csod-date:"+date+"\nx-csod-session-token:"+token+"\n/services/api/x/odata/api/views/vw_rpt_training"

    //def generateSignature(key, message, date, expected, algo)
    val signature = HMACgen.generateSignature(
      secret,
      stringToSign ,
      date,
      "",
      "HmacSHA512")

    println("\n\n\n\n\n")

    println("--------- REQUEST ODATA PARAMS ----------------")
    println("stringToSign :"+stringToSign)
    println("url : "+"https://socgen-stg.csod.com/services/api/x/odata/api/views/vw_rpt_training"  )
    val url = "url : "+"https://socgen-stg.csod.com/services/api/x/odata/api/views/vw_rpt_training" + "?$filter=lo_modified_dt=2013-4-3"
    val get: HttpGet = new HttpGet(url )
    get.addHeader(X_CSOD_DATE , date)
    get.addHeader(X_CSOD_SIGNATURE , signature)
    get.addHeader(X_CSOD_SESSION_TOKEN , token )
    get.addHeader(CONTENT_TYPE, TEXT_XML)
    //get.addHeader(ACCEPT, TEXT_XML)
    println("--------- GET REQUEST ODATA HEADER ----------------")
    get.getAllHeaders.foreach(arg => println(arg))

    val httpClient = new DefaultHttpClient()
    val httpResponse = httpClient.execute(get)
    println("--------- RESPONSE ODATA HEADER ----------------")
    httpResponse.getAllHeaders.foreach(arg => println(arg))
    val entity = httpResponse.getEntity()
    var content = ""
    if (entity != null) {
      val inputStream = entity.getContent()
      content = Source.fromInputStream(inputStream).getLines.mkString("\n")
      inputStream.close
    }
    println("--------- REQUEST ODATA RESULT ----------------")
    println(content)
  }










  def fakeAuth(): Unit ={
    val url = "https://socgen-stg.csod.com"
    val sessionUrl = "/services/api/sts/session"
    val userName = ApiCode.getProperty(ApiCode.USER_NAME)
    val httpMethod = "POST"
    val userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.52 Safari/537.17')"
    val apiId = ApiCode.getProperty(ApiCode.API_ID)// fourni par CSOD
    val apiSecret = ApiCode.getProperty(ApiCode.API_SECRET)

    val date = "2017-11-27T19:33:09.000"
    val signature = "+3pGsOKjhDLxcT2UK73j5UXFR9stYogjmtFBDxQgbSCBn0rYla/XGWz2L2TSsbuvOclhXpenVK6BWW06Od+QVA=="
    val alias = "8908ed87d960eb1acf8c69347571ac6a"

    logParams(userName, apiId, apiSecret, date, signature)

    val post: HttpPost = new HttpPost(url + sessionUrl + "?userName=" + userName + "&alias=" + alias)
    post.addHeader("x-csod-date" , date)
    post.addHeader("x-csod-signature" , signature)
    post.addHeader("x-csod-api-key" , apiId )
    post.addHeader("Content-Type" , "text/xml")
    post.addHeader("Accept" , "text/xml")
    post.expectContinue()

    logRequest(post)

    val httpClient = new DefaultHttpClient()
    // send the post request
    val httpResponse = httpClient.execute(post)
    println("--- HEADERS ---")
    httpResponse.getAllHeaders.foreach(arg => println(arg))
    println("--- BODY ---")
    val entity = httpResponse.getEntity()
    var content = ""
    if (entity != null) {
      val inputStream = entity.getContent()
      content = Source.fromInputStream(inputStream).getLines.mkString("\n")
      inputStream.close
    }
    println(content)
    httpClient.getConnectionManager().shutdown()
  }
}
