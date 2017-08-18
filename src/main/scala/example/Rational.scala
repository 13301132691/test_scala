package example

/**
  * Created by Administrator on 2017/6/12.
  */
class Rational(x: Int, y: Int) {
  require(y>0,"dominator must be positive")
  def this(x:Int) = this(x,1)
  private def gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)

  val numer = x / math.abs(gcd(x, y))

  val denom = y / math.abs(gcd(x, y))

  def + (that: Rational) = {
    new Rational(numer * that.denom + denom * that.numer, denom * that.denom)
  }

  def unary_-  = new Rational(-numer, denom)

  def - (that: Rational) = this + -that

  def * (that: Rational) = {
    new Rational(numer * that.numer, denom * that.denom)
  }

  def / (that: Rational): Rational = {
    val numer_new = numer * that.denom
    val denom_new = denom * that.numer
    if (denom_new == 0) throw new IllegalArgumentException("0 cannot be divided")
    new Rational(numer_new, denom_new)
  }

  def less(r:Rational) = this.numer * r.denom < r.numer * this.denom
  def max(r:Rational) = if(less(r)) r else this
  override def toString: String = numer + "/" + denom

}

object Rational extends App {
  implicit def intToRational(n:Int):Rational= new Rational(n)
//  val r = new Rational(1, 2)
//  val s = new Rational(1, 4)
//  println(r max s)
//  println(r less s)
//  println(r/s)
//  println(r+s)
//  println(r-s)
//  println(r*s)
//  println(r.gcd(-1,10))
//  println(2*r)
//  val d = new Rational(3)
//  println(d)
//  println(r*r+s*s)
//  def error(msg:String) = throw new Error(msg)
//  error("message")
  //assert(-1>0,"-1<0")

}
