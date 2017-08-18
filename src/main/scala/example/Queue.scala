package example

class Queue[T] private(private val leading: List[T], private val tailing: List[T]) {
  def this(elems: T*) = this(elems.toList, Nil)

  private val mirror = if (leading.isEmpty) new Queue[T](tailing.reverse, Nil) else this

  def head: T = mirror.leading.head

  def tail: Queue[T] = {
    val q = mirror
    new Queue[T](mirror.leading.tail, q.tailing)
  }

  def append(elems: T*) = new Queue[T](leading, elems.toList ::: tailing)

  override def toString: String = leading ::: tailing.reverse mkString("[", ",", "]")
}

object Queue {
  def apply[T](elems: T*) = new Queue[T](elems.toList, Nil)
}

object test extends App {
  val q = new Queue[Int](1, 2, 3, 4, 5)
}