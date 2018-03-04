import scala.xml.{Node, NodeSeq, XML}

object SimpleEdmxParser {
  val xml = XML.loadFile("D:\\DL\\s4s\\spark-scala-master\\spark-scala-master\\SparkTestFrameWork\\src\\main\\resources\\sfapi.edmx")

  val MAX_DEPTH = 4

  var encountered: Set[String] =   Set("FormContent")
  var counter = 0

  def main( args: Array[String] ): Unit = {
    down("FormContent", 0, "FormContent")
  }

  def down(childName: String, distance: Int, path: String): Unit ={
    println("Looking for: " + childName)
    val childEntity = (xml \\ "EntityType").filter(_.attribute("Name").exists(name => name.text == childName))
    val name = (childEntity \@"Name")
    val keys = (childEntity \ "Key" \ "PropertyRef" \@"Name")
    val properties = (childEntity \ "Property")
    val children = (childEntity \ "NavigationProperty")

    counter += 1
    println("["+path+"]")
    println(" - counter: " + counter)
    println(" - name: " + name)
    println(" - children: " + children.size)

    val pathWithKeys = path + keys.mkString("('", "','", "')")

    //processKeys(keys)
    processProperties(properties)

    if(distance < MAX_DEPTH)
      children.iterator.foreach(node => processNavigationProperty(node, distance + 1, pathWithKeys ))
  }

  def processKeys(keys: NodeSeq): Unit ={
    keys.foreach(
      key => println("   + " + key \@"Name")
    )
  }

  def processProperties(properties: NodeSeq): Unit ={
    properties.foreach(
      property => println("   - " + property \@"Name")
    )
  }

  def processNavigationProperty(node: Node, distance: Int, path: String): Unit ={
    val childName = (node \ "@ToRole").text

    if( mustGoDown(childName) ){
      println("Already contains !! ")
    }
    else{
      println("Does not exist => childName " + childName + "  NOT IN ["+encountered+"]")
      encountered = encountered + childName
      down(childName, distance, path + childName +"/" )
    }
  }

  def mustGoDown(childName: String): Boolean ={
    var result = true
    if(
      encountered.contains(childName) || !startsWithUpperCase(childName)
    ){
      result = false
    }
    result
  }

  def startsWithUpperCase(s: String): Boolean ={
    (s.charAt(0) >= 97 && s.charAt(0) <= 122)
  }
}
