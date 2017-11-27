package api

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter

import org.apache.commons.codec.binary.Base64

object HMACgen {
  val url = "https://socgen-stg.csod.com"
  val userName = ApiCode.getProperty(ApiCode.USER_NAME)
  val userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.52 Safari/537.17')"
  val apiId = ApiCode.getProperty(ApiCode.API_ID)
  val apiSecret = ApiCode.getProperty(ApiCode.API_SECRET)
  val algorithm = "HmacSHA512"

  def main( args: Array[String] ): Unit = {
    test1
    test2
    test3
  }

  def test1(): Unit ={
    val algo = "HmacSHA512"
    val date = "2017-11-24T19:53:49.000"
    val key = apiSecret
    val message = "POST\nx-csod-api-key:"+apiId+"\nx-csod-date:" + date + "\n/services/api/sts/session"
    val expected = "kgslsDKIM1RH9/ji8t3ytbzUoAFyV66qVp26oCk3w9Zaq5rJTWsFlYqfYWbStV3yC+5bllgmgo7JGbaD3JqrtA=="

    generateSignature(key: String, message: String, date: String, expected: String, algo: String)
  }

  def test2(): Unit ={
    val algo = "HmacSHA512"
    val date = "2017-11-26T11:47:42.000"
    val key = apiSecret
    val message = "POST\nx-csod-api-key:"+apiId+"\nx-csod-date:2017-11-26T11:47:42.000\n/services/api/sts/session"
    val expected = "ax2vxHau4XDFUW2Bp6hsSSMsSiiPd3Og/NDbqf7iSA/IoT4HLz89bfsVwYZAAlQ0Rcyd9QdU67Y68mnmMyA5ig=="

    generateSignature(key: String, message: String, date: String, expected: String, algo: String)
  }

  def test3(): Unit ={
    val algo = "HmacSHA512"
    val date = "2017-11-26T19:21:07.000"
    val key = apiSecret
    val message = "POST\nx-csod-api-key:"+apiId+"\nx-csod-date:2017-11-26T19:21:07.000\n/services/api/sts/session"
    val expected = "nohmA0+SkG4Dkf+eyHb5vHXTt3jttn0nHdMLAFo0ng6MjvW2P6eUPrh/tKc5aI8MPWCnrvhB3rBOZBQFLuOpuQ=="

    generateSignature(key: String, message: String, date: String, expected: String, algo: String)
  }

  def buildStringToSign(date: String, httpMethod: String, keyName: String, key: String, endPoint: String): String ={
    httpMethod + "\n" + keyName + ":" + key + "\n" + "x-csod-date:" + date + "\n" + endPoint
  }

  def generateSignature(key: String, message: String, date: String, expected: String, algo: String): String = {
    var encoded = ""

    try {
      val keyBytesDecoded = DatatypeConverter.parseBase64Binary(key)
      val messageBytes = message.getBytes("UTF-8")
      val HMAC = Mac.getInstance(algo)
      val secretKey = new SecretKeySpec(keyBytesDecoded, algo)
      HMAC.init(secretKey)
      val hash = HMAC.doFinal(messageBytes)
      encoded = Base64.encodeBase64String(hash)
    } catch {
      case e: Exception =>
        e.printStackTrace()
    }
    System.out.println("--- generateSignature ---")
    System.out.println("message: {\n"+ message + "\n }")
    System.out.println("encoded  : ["+encoded+"]")
    System.out.println("expected : ["+expected+"]")
    val goodResult = expected == encoded
    System.out.println("expected == encoded: "+goodResult)
    //assert(expected == encoded)
    encoded
  }
}
