//时间
import com.github.nscala_time.time.Imports._

import scala.util.Random
val d = DateTime.now
d.toString("MM/dd/yyyy hh:mm:ss.SSSa")
d+2.months
DateTime.nextMonth < DateTime.now + 2.months // returns Boolean = true
DateTime.now to DateTime.tomorrow  // return org.joda.time.Interval = > 2009-04-27T13:47:14.840/2009-04-28T13:47:14.840
(DateTime.now to DateTime.nextSecond).millis // returns Long = 1000
2.hours + 45.minutes + 10.seconds
// returns com.github.nscala_time.time.DurationBuilder
// (can be used as a Duration or as a Period)
(2.hours + 45.minutes + 10.seconds).millis
(2.hours + 45.minutes + 10.seconds).toDuration
// returns Long = 9910000
2.months + 3.days
// returns Period

import spire.syntax.literals._

// bytes and shorts
val x = b"100" // without type annotation!
val y = h"999"
val mask = b"255" // unsigned constant converted to signed (-1)

// rationals
val n1 = r"1/3"
val n2 = r"2/3"
n1 + n2

var b=BigDecimal("3.1415926")
b+2

Integer.parseInt("10000",2)
implicit class StringToInt(s:String) {
  def toInt(radix:Int) = Integer.parseInt(s,radix)
}
"1000".toInt(16)

implicit class MathUtils(dou:Double){
  def ~=(y:Double, precision:Double=0.0001): Boolean ={
    if((dou-y).abs<precision) true else false
  }
}
(0.1+0.2) == 0.3
(0.1+0.2) ~= 0.3

val r = Random
r.nextInt
r.nextInt(100)
r.nextFloat()
r.nextPrintableChar()

implicit class next(r:Random){
  val letters = 'A' to 'z'
  val len = letters.length
  def nextChar:Char = letters(r.nextInt(len))
}

r.nextChar
r.alphanumeric take 10 foreach println

val pi = scala.math.Pi
println(f"$pi%3.6f")
val formatter = java.text.NumberFormat.getIntegerInstance
formatter.format(123456789)
val locale = new java.util.Locale("de","DE")
val formatter2 = java.text.NumberFormat.getIntegerInstance(locale)
formatter2.format(123456789)
val formatter3 = java.text.NumberFormat.getInstance
formatter3.format(12345.6789)

val locale2 = new java.util.Locale("cn","CN")
val formatter4 = java.text.NumberFormat.getCurrencyInstance(locale2)
formatter4.format(123456.789)