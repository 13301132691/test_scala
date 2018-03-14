package algorithm.c1

object gcd {
  def gcd(p:Int,q:Int):Int = {
    var res = p
    if(q !=0){
      res = gcd(q,p%q)
    }
    res
  }

  def main(args: Array[String]): Unit = {
    print(gcd(10,6))
  }
}
