package example

/**
  * Created by jax on 17-6-22.
  */
abstract class JSON

case class JSeq(elems:List[JSON]) extends JSON
case class JObj(bindings:Map[String,JSON]) extends JSON
case class JNum(num:Int) extends JSON
case class JStr(str:String) extends JSON
case class JBool(b:Boolean) extends JSON
case class JNull(n:Null) extends JSON

object JSON extends App{
  val data = JObj(Map(
    "firstname"->JStr("John"),
    "lastname"->JStr("Smith"),
    "address"->JObj(Map(
      "streetAdress"->JStr("21 2nd Street"),
      "state"->JStr("NY"),
      "postalCode"->JNum(10021)
    )),
    "phoneNumbers"->JSeq(List(
      JObj(Map(
        "type" -> JStr("home"), "number" -> JStr("212 555-1234")
      )),
      JObj(Map(
        "type" -> JStr("fax"), "number" -> JStr("212 555-1235")
      ))
    ))
  ))


  def show(json:JSON):String = json match{
    case JObj(bindings) =>
      val assoc = bindings map{
        case (key,value) => "\"" + key + "\":" + show(value)
      }
      "{" + (assoc mkString ",") + "}"
    case JSeq(elems) => "[" + (elems map show mkString ",") + "]"
    case JNum(num) => num.toString
    case JStr(str) => "\"" + str + "\""
    case JBool(b) => b.toString
    case JNull(n) => "null"
  }
  print(show(data))

}

