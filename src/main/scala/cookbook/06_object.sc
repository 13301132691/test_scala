classOf[Int]
val stringClass = classOf[String]
stringClass.getMethods
val p = <p>jax</p>
p.getClass()
//package object model{
//  val e=1
//}

class Person{
  var name:String = _
  var age:Int = _
}
object Person{
  def apply(name:String):Person = {
    var p = new Person
    p.name = name
    p
  }
  def apply(name:String,age:Int):Person = {
    var p = new Person
    p.name = name
    p.age = age
    p
  }
}

trait Animal{
  def speak
}
object Animal{
  private class Dog extends Animal{
    override def speak: Unit = {
      print("woof")
    }
  }
  private class Cat extends Animal{
    override def speak: Unit = {
      print("Meow")
    }
  }

  def apply(s:String): Animal = {
    if(s=="cat") new Cat
    else new Dog
  }
}
Animal("cat").speak
