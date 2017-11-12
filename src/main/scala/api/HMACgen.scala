package api

import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.{Base64, Date}
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
object HMACgen {

  def generateHMAC(sharedSecret: String, stringToSign: String, algo: String): String = {
    val secret = new SecretKeySpec(sharedSecret.getBytes, algo)
    val mac = Mac.getInstance(algo)
    mac.init(secret)
    val hashString: Array[Byte] = mac.doFinal(stringToSign.getBytes)
    new String(hashString.map(_.toChar))
  }

  def main( args: Array[String] ): Unit = {
    println("eWFu:> "+ Base64.getDecoder.decode("eWFu"))
    println("generateHMAC:> "+ generateHMAC("eWFu", "eWFu", "HmacSHA512"))
    println(signatureForAuthentification("eWFu", "eWFu"))
  }

  def signatureForAuthentification(apiId: String, apiSecret: String): String ={
    //
    val httpMethod = "POST"
    val apiKey = "x-csod-api-key:"+apiId
    val httpUrl = "/services/api/sts/session"

    val outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss") // +".000"
    val date = new Date()
    val formattedDate = outputFormat.format(date)
    println("formattedDate: "+formattedDate)

    val stringToSign = httpMethod+"\n"+apiKey+"\n"+date+"\n"+httpUrl

    val secretKey = new String(Base64.getDecoder.decode(apiSecret), StandardCharsets.UTF_8)

    Base64.getEncoder.encodeToString(
      generateHMAC(secretKey, stringToSign, "HmacSHA512").getBytes(StandardCharsets.UTF_8)
    )
  }

  def signatureForRequest(token: String, url: String, apiSecret: String): String ={
    //
    val httpMethod = "POST"
    val apiKey = "x-csod-session-token:"+token
    val httpUrl = "/services/api/sts/session"

    val outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss") // +".000"
    val date = new Date()
    val formattedDate = outputFormat.format(date)
    println("formattedDate: "+formattedDate)

    val stringToSign = httpMethod+"\n"+apiKey+"\n"+date+"\n"+httpUrl

    val secretKey = new String(Base64.getDecoder.decode(apiSecret), StandardCharsets.UTF_8)

    Base64.getEncoder.encodeToString(
      generateHMAC(secretKey, stringToSign, "HmacSHA512").getBytes(StandardCharsets.UTF_8)
    )
  }

}
