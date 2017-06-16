package example

/**
  * Created by I330700 on 2017/6/13.
  */

object InSet{
  def main(args: Array[String]): Unit = {
    val em = Empty
    val tree1 = em.include(7).include(3)
    val tree2 = em.include(2).include(6).include(4)
    println(tree1)
    println(tree2)
    println(tree1.union(tree2))
  }
}

abstract class InSet {
  def contains(x: Int): Boolean

  def include(x: Int): InSet

  def union(other:InSet):InSet
}

object Empty extends InSet {
  def contains(x: Int): Boolean = false

  def include(x: Int): InSet = new NoEmpty(x, Empty, Empty)

  override def union(other: InSet): InSet = other

  override def toString: String = "."
}

class NoEmpty(elem: Int, left: InSet, right: InSet) extends InSet {
  override def contains(x: Int): Boolean = {
    if (x < elem) left contains x
    else if (x > elem) right contains x
    else true
  }

  override def include(x: Int): InSet = {
    if (x < elem) new NoEmpty(elem, left include x, right)
    else if (x > elem) new NoEmpty(elem, left, right include x)
    else this
  }

  override def union(other: InSet): InSet = (left union right) union other include elem
  override def toString: String = "{" + left + elem + right + "}"
}
