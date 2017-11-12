package api

object SoapClientTest {

    def doTest1 {
      println("############################################## ONE")
      val host = "https://apitest.authorize.net/soap/v1/Service.asmx"
      val req = <IsAlive xmlns="https://api.authorize.net/soap/v1/"/>
      val cli = new SoapClient
      println("##### request:\n" + cli.wrap(req))
      val resp = cli.sendMessage(host, req)
      if (resp.isDefined) {
        println("##### response:\n" + resp.get.toString)
      }
    }

    def doTest2 {
      println
      println
      println("############################################## TWO")
      val host = "http://ws.cdyne.com/WeatherWS/Weather.asmx"
      val req = <GetCityForecastByZIP xmlns="http://ws.cdyne.com/WeatherWS/">
        <ZIP>77058</ZIP>
      </GetCityForecastByZIP>
      val cli = new SoapClient
      println("##### request:\n" + cli.wrap(req))
      val resp = cli.sendMessage(host, req)
      if (resp.isDefined) {
        println("##### response:\n")
        (resp.get \\ "Forecast").foreach(elem => {
          println("#########\n" + elem.toString)
        })
      }
    }

    def main( args: Array[String] ): Unit = {
      doTest1
      doTest2
    }

}
