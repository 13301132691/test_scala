package example

/**
  * Created by I330700 on 2017/6/3.
  */
object Sqrt extends App {

  def sqrt(x: Double): Double = {
    def abs(x: Double) = if (x < 0) -x else x

    def sqrtIter(guess: Double): Double = {
      if (isGoodEnough(guess)) guess
      else sqrtIter(improve(guess))
    }

    def isGoodEnough(guess: Double): Boolean =
      if (x != 0.0) abs((guess * guess - x) / x) < 0.0001
      else abs(guess * guess - x) < 0.0001

    def improve(guess: Double): Double = (guess + x / guess) / 2

    sqrtIter(1.0)
  }

  val y = sqrt(2)
  println(y)

  def factorial(n: Int): Int = {
    def loop(acc: Int, n: Int): Int = {
      if (n == 0) acc else loop(acc * n, n - 1)
    }
    loop(1, n)
  }

  var z = factorial(4)
  println(z)
}
