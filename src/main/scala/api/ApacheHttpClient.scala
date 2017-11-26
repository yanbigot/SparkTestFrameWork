package api

import api.ODataClient.getDate
import org.apache.http.HttpVersion
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient

import scala.io.Source

object ApacheHttpClient {
  val url = "https://socgen-stg.csod.com"
  val sessionUrl = "/services/api/sts/session"
  val userName = ApiCode.getProperty(ApiCode.USER_NAME)
  val httpMethod = "POST"
  val userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.52 Safari/537.17')"

  val post = new HttpPost(url + sessionUrl)
  val apiId = ApiCode.getProperty(ApiCode.API_ID)// fourni par CSOD
  val apiSecret = ApiCode.getProperty(ApiCode.API_SECRET)
  val date = getDate
  val signature = HMACgen.generateSignature(apiSecret: String, HMACgen.buildStringToSign(date): String, date: String, "": String, "HmacSHA512": String)

  def main(args: Array[String]) {
    val post: HttpPost = new HttpPost(url + sessionUrl + "?userName=" + userName + "&alias=" + date)
    //post.addHeader("Connection" , "Keep-Alive")
    post.addHeader("x-csod-date" , date)
    post.addHeader("x-csod-signature" , signature)
    post.addHeader("x-csod-apiKey" , apiId )
    post.addHeader("Content-Type" , "text/xml")
    post.addHeader("Accept" , "text/xml")
    post.setProtocolVersion(HttpVersion.HTTP_1_1)
    println("--- REQUEST METHOD---")
    println(post.getMethod())
    println("--- REQUEST HEADERS---")
    post.getAllHeaders.foreach(arg => println(arg))
    println("--- REQUEST getProtocolVersion---")
    println(post.getProtocolVersion)
    println("--- REQUEST getRequestLine---")
    println(post.getRequestLine)


    val httpClient = new DefaultHttpClient()
//    val params = client.getParams
//    params.setParameter("userName", userName)
//    params.setParameter("alias", date)

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
