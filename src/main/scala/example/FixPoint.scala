package example

import scala.math.abs

/**
  * Created by I330700 on 2017/6/10.
  */
object FixPoint extends App {
  val tolerance = 0.0001

  def isCloseEnough(guess: Double, x: Double): Boolean = {
    abs((guess - x) / x) < tolerance
  }

  def fixPoint(f: Double => Double)(firstGuess: Double): Double = {
    def iterate(guess: Double): Double = {
      val next = f(guess)
      if (isCloseEnough(guess, next)) next
      else iterate(next)
    }

    iterate(firstGuess)
  }

  def sqrt(x: Int): Double = fixPoint(y => (y + x / y) / 2)(x)
  println(sqrt(4))
  def test(x:Double):Double = fixPoint(x=>1+x/2)(x)
  print(test(1))
}
