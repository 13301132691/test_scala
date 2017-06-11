package example

/**
  * Created by I330700 on 2017/6/10.
  */
object HighOrderCurrying extends App{
    def sum(f:Int=>Int):(Int,Int)=>Int={
      def sumF(a:Int,b:Int): Int ={
        if(a>b) 0
        else f(a) + sumF(a+1,b)
      }
      sumF
    }
/**
  * def sum(f:Int=>Int,a:Int,b:Int):Int={
      if(a>b) 0 else f(a) + sum(f,a+1,b)
    }
  * */
    def sumInt = sum(x=>x)
    def sumCube = sum(x=>x*x*x)
    def sumFactorial = sum(factorial)
    def factorial(x:Int):Int = if(x==1) 1 else x+factorial(x-1)
    print(sumFactorial(1,2))
  def mapReduce(f:Int=>Int,combine:(Int,Int)=>Int,zero:Int)(a:Int,b:Int):Int={
    if(a>b) zero
    else combine(f(a),mapReduce(f,combine,zero)(a+1,b))
  }
}