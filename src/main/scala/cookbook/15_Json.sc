import net.liftweb.json._
import net.liftweb.json.Serialization.write
import com.google.gson.Gson

case class Person(name:String,address:Address)
case class Address(city:String,state:String)
val p = Person("jax",Address("bj","bj"))
implicit val formats = DefaultFormats
val jsonString = write(p)
print(jsonString)

val gson = new Gson
val jsonString2 = gson.toJson(p)

import net.liftweb.json.JsonDSL._
import net.liftweb.json.{compactRender,prettyRender}

val json = List(1,2,3)
compactRender(json)
prettyRender(json)
val map = Map("fname"-> "a","addr"->"beijing")
compactRender(map)
