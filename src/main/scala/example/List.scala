//package example
//
///**
//  * Created by I330700 on 2017/6/13.
//  */
//trait List[T] {
//  def isEmpty:Boolean
//  def head:T
//  def tail:List[T]
////  def isort(list: List[Int]):List[Int] = list match{
////    case List() => List()
////    case x::xs => insert(x,isort(xs))
////  }
////  def insert(x:Int,xs:List[Int]):List[Int] = xs match{
////    case List() => List(x)
////    case y::ys => if(x <= y) x::xs else y::insert(x,ys)
////  }
//}
//
//class Nil[T] extends  List[T]{
//  override def isEmpty: Boolean = true
//
//  override def head: Nothing = throw new NoSuchElementException("Nil.head")
//  override def tail: Nothing = throw new NoSuchElementException("Nil.tail")
//}
//
//class Cons[T](val head:T,val tail:List[T])extends List[T]{
//  def isEmpty:Boolean = false
//}
//
//object List extends App{
//  def singleton[T](elem:T):List[T]=new Cons[T](elem,new Nil[T])
//
//  def nth[T](n:Int,xs:List[T]):T = {
//    if(xs.isEmpty) throw new IndexOutOfBoundsException("")
//    else if(n == 1) xs.head
//    else nth(n-1,xs.tail)
//  }
//
//  val list = new Cons(12,new Cons(14,new Cons(16,new Cons(18,new Nil))))
//  println(nth(3,list))
//}
