package example


/**
  * Created by Administrator on 2017/6/22.
  */
trait Generator[ +T ] {
  self =>
  def generate: T

  def map[ S ](f: T => S): Generator[ S ] = new Generator[ S ] {
    override def generate: S = f(self.generate)
  }

  def flatMap[ S ](f: T => Generator[ S ]): Generator[ S ] = new Generator[ S ] {
    override def generate: S = f(self.generate).generate
  }
}

object Generator extends App {
  val integers = new Generator[ Int ] {
    def generate = scala.util.Random.nextInt()
  }
  val booleans = for (x <- integers) yield x > 0

  // val booleans = integers map {x=>x>0}
  def pairs[ T, U ](t: Generator[ T ], u: Generator[ U ]) = (t, u) match {
    case (x, y) => (x, y)
  }

  def single[ T ](x: T): Generator[ T ] = new Generator[ T ] {
    override def generate: T = x
  }

  def choose(lo: Int, hi: Int): Generator[ Int ] = for (x <- integers) yield lo + x % (hi - lo)

  def oneOf[ T ](xs: T*): Generator[ T ] = for (idx <- choose(0, xs.length)) yield xs(idx)

  def lists: Generator[ List[ Int ] ] = for {
    isEmpty <- booleans
    list <- if (isEmpty) emptyLists else nonEmptyLists
  } yield list

  def emptyLists = single(Nil)

  def nonEmptyLists = for {
    head <- integers
    tail <- lists
  } yield head :: tail

  trait Tree
  case class Inner(left:Tree,right:Tree) extends Tree
  case class Leaf(x:Int) extends Tree
  def leafs:Generator[Tree] = for(x<-integers)yield Leaf(x)
  def inners:Generator[Tree] = for{
    l <- trees
    r <- trees
  }yield Inner(l,r)
  def trees:Generator[Tree] = for{
    isLeaf <- booleans
    tree <- if(isLeaf) leafs else inners
  }yield tree
  trees.generate
}


