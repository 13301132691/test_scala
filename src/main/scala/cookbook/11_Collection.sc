import scala.collection.immutable.ListMap
import scala.collection.mutable.ListBuffer

List.fill(4)("jax")
List.tabulate(5)(n=>n * n)
"jax".toList
1::2::33::44::55::Nil
0 +: (1::2::Nil)
var lb = ListBuffer(1,2,3,4,5)
lb += 6
lb ++= 2::4::Nil
lb.remove(5)

val s = 1#::2#::3#::4#::5#::Stream.empty
s.tail
s.take(2)

scala.util.Sorting.quickSort(lb.toArray)

val m1 = Map((1,"a"),(2,"b"))
m1.mapValues(_.toUpperCase)
m1.transform((k,v)=>k+v)
ListMap(m1.toArray.sortBy(_._1):_*)
ListMap(m1.toArray.sortWith(_._2 > _._2):_*)
m1.max
m1.valuesIterator.max
var set = collection.mutable.Set(1,2,3,4,5)
set.retain(_>2)
set
