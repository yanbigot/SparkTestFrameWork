name := "SparkTestFrameWork"

version := "0.1"

scalaVersion := "2.11.11"

// https://mvnrepository.com/artifact/org.apache.spark/spark-sql_2.10
libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "2.2.0"

// https://mvnrepository.com/artifact/org.apache.spark/spark-sql_2.10
libraryDependencies += "org.apache.spark" % "spark-sql_2.11" % "2.2.0"

// https://mvnrepository.com/artifact/org.scalatest/scalatest_2.11
libraryDependencies += "org.scalatest" % "scalatest_2.11" % "3.0.4" % Test

// https://mvnrepository.com/artifact/org.pegdown/pegdown
libraryDependencies += "org.pegdown" % "pegdown" % "1.6.0" % Test

// https://mvnrepository.com/artifact/info.cukes/cucumber-scala_2.11
libraryDependencies += "info.cukes" % "cucumber-scala_2.11" % "1.2.5"

// https://mvnrepository.com/artifact/info.cukes/cucumber-junit
libraryDependencies += "info.cukes" % "cucumber-junit" % "1.2.5" % Test

// https://mvnrepository.com/artifact/info.cukes/cucumber-picocontainer
libraryDependencies += "info.cukes" % "cucumber-picocontainer" % "1.2.5" % Test

// https://mvnrepository.com/artifact/junit/junit
libraryDependencies += "junit" % "junit" % "4.8.2" % Test

// https://mvnrepository.com/artifact/com.novocode/junit-interface
libraryDependencies += "com.novocode" % "junit-interface" % "0.10" % Test

// https://mvnrepository.com/artifact/org.seleniumhq.selenium/selenium-java
libraryDependencies += "org.seleniumhq.selenium" % "selenium-java" % "3.6.0"

// https://mvnrepository.com/artifact/com.holdenkarau/spark-testing-base_2.10
libraryDependencies += "com.holdenkarau" %% "spark-testing-base" % "2.2.0_0.8.0" % Test

// https://mvnrepository.com/artifact/org.apache.spark/spark-mllib_2.11
libraryDependencies += "org.apache.spark" % "spark-mllib_2.11" % "2.2.0" % "provided"

// https://mvnrepository.com/artifact/com.stackmob/newman
libraryDependencies += "com.stackmob" %% "newman" % "1.3.5"

libraryDependencies += "org.scalaj" % "scalaj-http_2.11" % "2.3.0"

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-html-reports")

