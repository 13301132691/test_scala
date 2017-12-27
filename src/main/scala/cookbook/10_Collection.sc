List.range(1,10)
val mm = collection.mutable.Map()
val ms = collection.mutable.Set(1,2,3)

var v = Vector(1,2,3)
v = v ++ Vector(4)
v = v.updated(0,2)

for((elem,count) <- v.zip(Stream from 1)){
  println(s"the $count th elem is $elem")
}

v.zipWithIndex.foreach({
  case(num,count) => println(s"$count is $num")
})

val list= List("Hello","world")
list.flatten

val s = Vector(Some(1),Some(2),None,Some(4))
s.flatten

val bag = List("1","3","4","three","five","6")

def toInt(in:String):Option[Int] = {
  try{
    Some(Integer.parseInt(in.trim))
  }catch{
    case e: Exception => None
  }
}

bag.flatMap(toInt).sum

//slice.(from,until)

val y = v.groupBy(_>2)
val a = (1 to 10).toArray
a.sliding(3).map(_.toList).toList

val b = (1 to 10) zip (1 to 10)
b.unzip

a.reduceLeft(_+_)
a.foldLeft(20)(_+_)
a.scanLeft(20)(_+_)

//实现了equals和hashCode的object可用distinct

a.toSet  -- v.toSet

val vi = (1 to 100).view
val x = vi.force

//Thread.sleep(10)

//spark的tranformer是否是通过view实现的

List.range(1,10)
Array.range(1,10)
Vector.range(1,10)
('a' to 'f').toList

(1 to 10).map(e=>(e,e+1)).toMap

object Margin extends Enumeration {
  type Margin = Value
  val TOP, BOTTOM, LEFT, RIGHT = Value

}
 case class Person(name:String) extends Ordered[Person]{
   override def toString: String = name

   def compare2(that:Person) = this.name.compare(that.name)

   override def compare(that: Person): Int = {
     if(this.name == that.name) 0
     else if(this.name > that.name) 1
     else -1
   }
 }