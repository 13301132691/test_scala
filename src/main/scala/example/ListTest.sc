
trait List1[ T ] {
  def isEmpty1: Boolean

  def head1: T

  def tail1: List1[ T ]
}

class Cons1[ T ](val head1: T, val tail1: List1[ T ]) extends List1[ T ] {
  override def isEmpty1 = false
}

class Nil1[ T ] extends List1[ T ] {
  override def isEmpty1 = true

  def head1 = throw new NoSuchElementException("Nil's head")

  def tail1 = throw new NoSuchElementException("Nil's tail")
}

object List1 extends App{
  def singleton[ T ](elem: T) = new Cons1[ T ](elem, new Nil1[ T ])
  def apply[T](x1:T,x2:T):List1[T] = new Cons1[T](x1,new Cons1[T](x2,new Nil1))

  def apply[T]() = new Nil1
  singleton[ Int ](1)
  singleton(true)
}
