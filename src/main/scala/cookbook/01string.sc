

val s = "hello jax"
s.foreach(println)
s.getBytes().filter(_%2 ==0)
"scala".drop(2).take(2).capitalize.toUpperCase
val sp =
  """
    |I am
    |good
    |boy
  """.stripMargin
val spp =
  """
    |I am
    |good
    |boy
  """.stripMargin.replaceAll("\n"," ")
val name = "jax"
println(s"$name is a boy")
val age = 33
println(s"${age+1}")
println(f"$age%.2f")
println(raw"jax\nis good")

//println("%s is %d years old").format(name,age) //在scala 2.10前可用

val toLower = (c:Char) => (c.toByte+32).toChar //function
def toLower2(c:Char):Char = (c.toByte+32).toChar //method
val p = "sss".foreach(_.toUpper) //foreach returns unit

val pattern = "[0-9]+".r
val addr = "1n2n3n4n5n5n6n7n8n"
val m1 = pattern.findFirstIn(addr)
pattern.findAllIn(addr).toList.foreach(println)

m1 match{
  case Some(s) => println(s"found: $s")
  case None => println("no match")
}

"[0-9]".r replaceAllIn ("123 main", "x")

val pa = """([0-9]+)\s([a-zA-Z]+)""".r
val pa(num,str) = "100 banana"
"100 banana" match{
  case pa(num,str) => println(s"there are $num $str")
}
"hello".charAt(3)
"hello"(3)

implicit class StringImprovements(s:String){
  def increment = s.map(c=>(c.toByte +1).toChar)
}
"HAL".increment
's'+1

implicit class st(str:String){
  def deng(b:Boolean) :Boolean = str match{
    case "0" | "zero" |""|" " => false == b
    case _ => true == b
  }
}
st("") deng false
"" deng false

implicit class StringImprovement(s:String){
  def asBoolean:Boolean = s match {
    case "0" | "zero" |""|" " => false
    case _ => true
  }
  def -(i:Int) :Int = s match {
    case "0" | "zero" |""|" " => 0 - i
    case _ => 1 - i
  }
}

implicit def asInt(b:Boolean):Int = b match{
  case false => 0
  case true  => 1
}
2 - false ==3 - true

"".asBoolean
"dd".asBoolean

"dd" - 1