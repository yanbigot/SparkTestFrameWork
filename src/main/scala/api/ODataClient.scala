package api

import java.text.SimpleDateFormat
import java.util.{Calendar, TimeZone}

import org.apache.http.client.methods.HttpPost

import scala.util.parsing.json._
import scalaj.http.{Http, HttpOptions}
object ODataClient {

  val url = "https://socgen-stg.csod.com"
  val sessionUrl = "/services/api/sts/session"
  val userName = "cesadmin"
  val httpMethod = "POST"
  val userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.52 Safari/537.17')"

  val post = new HttpPost(url + sessionUrl)
  val apiId = ApiCode.getProperty(ApiCode.API_ID)
  val apiSecret = ApiCode.getProperty(ApiCode.API_SECRET)

  def main( args: Array[String] ): Unit = {
    //authenticate
    //getMetadata

    val alias  ="2017-11-26T19:38:30.000"
    val date = "2017-11-26T19:38:30.000"
    val tokenDummy  = "mz4m7keua0oy"
    val signatureDummy = "iJMeO0zfzSCtopNZkmWYVCnlLzbCHo31eK7ULgIMYQUu5/qvgcu6BaJZkh4dhm+4H0y+Z0rVnNVU03ek3EnyYw=="
    val view = "vw_rpt_training"
    val filter = "$filter(lo_modified_dt=2017-10-23)"
    getTrainings(tokenDummy, signatureDummy, alias, date, view, filter, "")
  }

  def getTrainings(token: String,
                   signature: String,
                   alias: String,
                   date: String,
                   view: String,
                   filter: String,
                   urlSkipToken: String
                  ):String = {
    var url = "https://socgen-stg.csod.com/services/api/x/odata/api/views/"+view+"?" + filter
    if( ! urlSkipToken.isEmpty){
      url = urlSkipToken
    }
    val request = Http(url)
      .param("alias", alias)
      .method("GET")
      .header("x-csod-date",date)
      .header("x-csod-session-token",token)
      .header("x-csod-signature",signature)
      .header("Content-Type","text/xml")

    val response = request.asString
    println(response.body.toString.subSequence(0,100))
    val parsed = JSON.parseFull(response.body)
    val globalMap = parsed.get.asInstanceOf[Map[String, Any]]
    val nextLink:Any = globalMap.get("@odata.nextLink").orNull
    println("nextLink ["+globalMap.get("@odata.nextLink").orNull +"]")

    response.body
  }

  def processNextLink(nextLink: Any): Unit ={
    if (nextLink != null && nextLink.isInstanceOf[String]) {
      val request = Http(nextLink.toString)
      val response = request.asString
      println("NEXT LINK")
      println(response.body)
    }
  }

  def getMetadata():String = {
    val alias  ="2017-11-25T14:19:25.000"
    val token  = "5ie89ksmqvsn"
    val signature = "f6L0t1C96lgJfTzIzbQgQuXYmNGLAPbxR11RWLxJw0wTkrGhh8pb8X7Ezu0XnQiJXTVgrZLHaENpYlz0KqTmHA=="

    val request = Http("https://socgen-stg.csod.com/services/api/x/odata/api/views/$metadata")
      .header("Method", "GET")
      .header("x-csod-date",getDate)
      .header("x-csod-session-token",token)
      .header("x-csod-signature",signature)

    val response = request.asString

    println(response.body.subSequence(0, 300))
    response.body
  }

  def getHeaders(date: String, signature: String): Map[String, String] = {
    Map(
      "Method" -> "POST",
      "Connection" -> "Keep-Alive",
      "x-csod-date" -> date,
      "x-csod-signature" -> signature,
      "x-csod-api-key" -> apiId,
      "Content-Type" -> "text/xml"
    )
  }

  def fakeAuth(fakeDate: String, fakeSignature: String): String ={
    val request = Http(url+sessionUrl)
      .param("userName", userName)
      .param("alias", fakeDate)
      .method("POST")
      .headers(getHeaders(fakeDate: String, fakeSignature: String))
      .option(HttpOptions.followRedirects(true))

    val response = request.postData("").asString

    println(request)
    println(response)

    response.body
  }

//  def authenticate():String = {
//    val date = getDate()
//    val fakeDate = "2017-11-26T11:30:17.000"
//    val fakeSign = "HVAPBrW9EsZ0Sh2iWCiqtWuRejnb0pBlTuBJpvIyLklaohwYdWT3Ufj8S7mShygnBwkKlk7V3hlkScyknjlOMw=="
//    val message = HMACgen.buildStringToSign(date)
//    val signed = HMACgen.generateSignature(apiSecret: String, message: String, date: String, "": String, "HmacSHA512": String)
//    val request = Http(url+sessionUrl)
//      //  val request = Http(url+sessionUrl+"?userName="+userName + "&alias="+date)
//      .param("userName", userName)
//      .param("alias", date)
//      .method("post")
//      //.header("User-Agent", userAgent)
//      //.header("Method", "POST")
//      .header("Connection","Keep-Alive")
//      .header("x-csod-date",date)
//      .header("x-csod-signature",signed)
//      .header("x-csod-api-key",apiId)
//      .header("Content-Type","text/xml")
//      .header("Accept","text/xml")
//
//    val response = request.postForm.asString
//
//    println(request.url)
//    println(request.headers.mkString)
//    println(response.body.mkString)
//    println(response.headers)
//
//    response.body
//  }

  def getDate(): String ={
    //date('Y-m-d').'T'.date('H:i:s').'.000'
    val today = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime()

    // create the date/time formatters
    val yearMonthDay = new SimpleDateFormat("YYYY-MM-dd")
    val hour = new SimpleDateFormat("HH")
    val minutes = new SimpleDateFormat("mm")
    val secondes = new SimpleDateFormat("ss")

    val currentHourMinus1 = hour.format(today).toInt - 1
    val currentSecondesMinus4 = secondes.format(today).toInt - 4
    val currentYearMonthDay = yearMonthDay.format(today)
    val currentMinutes = minutes.format(today)

    val result = new String(currentYearMonthDay + "T" + currentHourMinus1 +":"+ currentMinutes +":"+ currentSecondesMinus4 +".000")
    println(result)
    result
  }
}
