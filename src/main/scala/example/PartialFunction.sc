

val f:PartialFunction[List[Int],String] = {
  case Nil => "one"
  case x::y::rest => "two"
}
val f2:PartialFunction[List[Int],String] = {
  case Nil => "one"
  case x::rest => rest match{
    case Nil => "two"
  }
}
f.isDefinedAt(List(1,2,3))
f2.isDefinedAt(List(1,2,3))
//f2(List(1,2,3))
def isPrime(n:Int) = if (n<=3) true else (2 until n/2+1) forall (i=>n%i!=0)
isPrime(5)
def sumPrime(n:Int):IndexedSeq[(Int, Int)] = {
  (1 to n) flatMap(i=>
    (1 to i) filter(j=>isPrime(i+j)) map (j=>(i,j))
    )
}
def sumPrime2(n:Int):IndexedSeq[(Int, Int)] = {
  for{
    i <- 1 until n
    j <- 1 until i
    if isPrime(i+j)
  }yield (i,j)
}
sumPrime(7)
sumPrime2(7)
