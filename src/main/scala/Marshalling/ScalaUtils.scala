package marshalling

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper


object JsonUtil {

  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  var flavor_list: FlavorList=_

  def toJson(value: Map[Symbol, Any]): String = {
    toJson(value map { case (k,v) => k.name -> v})
  }

  def toJson(value: Any): String = {

    return mapper.writeValueAsString(value)

  }

  def JsontoString(value: Any): String = {
      // nova JSON response as string
      val result=mapper.writeValueAsString(value)
      flavor_list= mapper.readValue(result, classOf[FlavorList])
      print(flavor_list.get_list())
    //lets see what we get
    return result

  }

  def StringtoFlavor(value: String): List[Flavor] = {
    // nova JSON response as string
    flavor_list= mapper.readValue(value, classOf[FlavorList])
    print(flavor_list.get_list())
    //lets see what we get
    return flavor_list.get_list()

  }


  def toMap[V](json:String)(implicit m: Manifest[V]) = fromJson[Map[String,V]](json)

  def fromJson[T](json: String)(implicit m : Manifest[T]): T = {
    mapper.readValue[T](json)
  }

}
