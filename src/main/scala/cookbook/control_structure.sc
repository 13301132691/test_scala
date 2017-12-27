val a = Array("apple","orange","banana")
for (i <- a) yield {
  val s = i.toUpperCase
  "{"+s+"}"
}
for((e,count) <- a.zipWithIndex){
  println(s"$count is $e")
}


import scala.Boolean
import util.control.Breaks._

breakable{
  for (i<-1 to 10){
    println(i)
    if(i>5) break
  }
}


import scala.annotation.tailrec

def factorial(n:Int):Int = {
  @tailrec def factorialAcc(acc:Int,n:Int):Int = {
    if(n<=1) acc
    else factorialAcc(n*acc,n-1)
  }
  factorialAcc(1,n)
}

def toInt(s:String): Option[Int] = {
  try{
    Some(Integer.parseInt(s.trim))
  }catch{
    case e : Exception=> None
  }
}


object CopyBytes extends App{
  import java.io._
  var in = None:Option[FileInputStream]
  var out = None:Option[FileOutputStream]

  try{
    in = Some(new FileInputStream("/tmp/test.class"))
    out = Some(new FileOutputStream("/tmp/test.class.copy"))
    var c= 0
    while({c = in.get.read();c!=1}){
      out.get.write(c)
    }
  }
  catch {
    case e: IOException => e.printStackTrace()
  }finally {
    if(in.isDefined) in.get.close()
    if(out.isDefined) out.get.close()
  }
}

@tailrec
def whist(testCondition: => Boolean)(codeBlock: => Unit){
  if(testCondition){
    codeBlock
    whist(testCondition)(codeBlock)
  }
}
val e = List(1,2,3,4)
e match{
  case list @ List(1,_*) => print(s"$list")
}
1 match {
  case num if 1 to 5 contains num => println(num)
}
// 不用将matchy语句包含在{}中
def sum(f:List[Int]): Int = f match {
    case n :: rest => n +  sum(rest)
    case Nil => 0
  }
sum(e)
