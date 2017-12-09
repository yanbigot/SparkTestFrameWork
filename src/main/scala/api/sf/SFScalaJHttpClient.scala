package api.sf

import scala.xml.XML
import scalaj.http.{Http, HttpRequest, HttpResponse}

object SFScalaJHttpClient {

  var sessionId = ""
  var status = ""
  var sessionCookie = ""
  var HEADERS =     Map(
    "Authorization" -> BASIC_AUTH,
    "Content-Type" -> "text/xml",
    "Connection" -> "keep-alive",
    "Cache-Control" -> "no-cache"
  )

  val FINISHED = "finished"
  val SUBMITTED = "submitted"
  val MAX_ATTEMPTS = 30
  val BASIC_AUTH = SFApiCode.getProperty(SFApiCode.BASIC_AUTH)
  val USERNAME = SFApiCode.getProperty(SFApiCode.USERNAME)
  val COMPANY_ID = SFApiCode.getProperty(SFApiCode.COMPANY_ID)
  val PASSWORD = SFApiCode.getProperty(SFApiCode.PASSWORD)
  val URL = SFApiCode.getProperty(SFApiCode.URL)



  def main( args: Array[String] ): Unit = {
    val sessionId = basicAuth(COMPANY_ID, USERNAME, PASSWORD)
    if (sessionId != "") {
      Thread.sleep(1000)
      val taskId = submitQuery(sessionId)
      var attemps = 0
      var allGood = false
      while(allGood == false && attemps < MAX_ATTEMPTS){
        println("WAITING 2 sec ...")
        Thread.sleep(2000)
        allGood = getJobStatus(taskId)
        attemps += 1
      }
      getJobResult(taskId)
    }
  }

  /**
    * This is pretty dangerous
    * @param companyId
    * @param username
    * @param password
    * @return
    */
  def basicAuth( companyId: String, username: String, password: String): String = {
    val reqBody =
      <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:urn="urn:sfobject.sfapi.successfactors.com">
        <soapenv:Header/>
        <soapenv:Body>
          <urn:login>
            <urn:credential>
              <urn:companyId>{companyId}</urn:companyId>
              <urn:username>{username}</urn:username>
              <urn:password>{password}</urn:password>
            </urn:credential>
          </urn:login>
        </soapenv:Body>
      </soapenv:Envelope>

    val request = Http(URL)
      .headers(HEADERS)
      .postData(reqBody.mkString)

    val response = request.asString
    logCall("AUTHENTICATION", request, response)
    retrieveSessionId(response.body).getOrElse("sessionId", "")
  }
  def retrieveSessionId( responseBody: String ): Map[String, String] = {
    val xml = XML.loadString(responseBody)
    val sessionId = (xml \\ "sessionId") text
    //should be used redoing the call and not reauth
    val msUntilPwdExpiration = (xml \\ "msUntilPwdExpiration") text

    Map(
      "sessionId" -> sessionId,
      "msUntilPwdExpiration" -> msUntilPwdExpiration
    )
  }

  def submitQuery( jSession: String ): String = {
    val reqBody =
      <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:urn="urn:sfobject.sfapi.successfactors.com">
        <soapenv:Header/>
        <soapenv:Body>
          <urn:submitQueryJob>
            <urn:queryString>
              SELECT LT_FORM_DATA_FORM_TEMPLATE_ID FROM ADHOC_pm
            </urn:queryString>
            <urn:param>
              <urn:name>maxRows</urn:name>
              <urn:value>10</urn:value>
            </urn:param>
          </urn:submitQueryJob>
        </soapenv:Body>
      </soapenv:Envelope>.mkString

    sessionCookie = new String("JSESSIONID=" + jSession + "; Path=/sfapi/; Secure")
    HEADERS += ("Cookie" -> sessionCookie)

    val request = Http(URL)
      .headers(HEADERS)
      .timeout(30000, 30000)
      .postData(reqBody)

    val response = request.asString
    logCall("SUBMIT QUERY", request, response)
    retrieveTaskId(response.body).getOrElse("taskId", "")
  }
  def retrieveTaskId( responseBody: String ): Map[String, String] = {
    val xml = XML.loadString(responseBody)
    val taskId = (xml \\ "taskId") text
    val taskName = (xml \\ "taskName") text
    val status = (xml \\ "status") text

    Map(
      "taskId" -> taskId,
      "taskName" -> taskName,
      "status" -> status
    )
  }

  def getJobStatus( taskId: String ): Boolean = {
    val reqBody =
      <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:urn="urn:sfobject.sfapi.successfactors.com">
        <soapenv:Header/>
        <soapenv:Body>
          <urn:getJobStatus>
            <urn:taskId>{taskId}</urn:taskId>
          </urn:getJobStatus>
        </soapenv:Body>
      </soapenv:Envelope>.mkString

    val request = Http(URL)
      .headers(HEADERS)
      .timeout(30000, 30000)
      .postData(reqBody)

    val response = request.asString
    logCall("GET JOB STATUS", request, response)
    isJobFinished(response.body)
  }
  def isJobFinished( responseBody: String ): Boolean = {
    var result = false
    val xml = XML.loadString(responseBody)
    val status = (xml \\ "taskResult" \ "status") text

    println("IS JOB FINISHED: "+status)

    if (status != null && status.length > 0) {
      result = status == (FINISHED)
    }
    result
  }

  def getJobResult(taskId: String): Unit ={
    val reqBody =
      <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                        xmlns:urn="urn:sfobject.sfapi.successfactors.com">
        <soapenv:Header/>
        <soapenv:Body>
          <urn:getJobResult>
            <urn:taskId>{taskId}</urn:taskId>
            <urn:format>csv</urn:format>
          </urn:getJobResult>
        </soapenv:Body>
      </soapenv:Envelope>.mkString

    val request = Http(URL)
      .headers(HEADERS)
      .timeout(30000, 30000)
      .postData(reqBody)

    val response = request.asString
    logCall("GET JOB RESULT", request, response)
  }

  def logCall(method: String ,request: HttpRequest,response: HttpResponse[String]): Unit ={
    println(s"********************* $method *********************************")
    println("\n--- REQUEST ---")
    println("--- REQUEST HEADERS ---")
    request.headers.foreach(println)
    println("\n--- RESPONSE ---")
    println("--- RESPONSE COOKIES ---")
    response.cookies.mkString("\n")
    println("--- RESPONSE HEADERS ---")
    response.headers.foreach(println)
    println("--- RESPONSE BODY ---")
    println(response.body)
  }
}
