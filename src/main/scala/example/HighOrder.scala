package example

/**
  * Created by I330700 on 2017/6/10.
  */
object HighOrder extends App{
    def sum(f:Int=>Int,a:Int,b:Int):Int={
      def loop(a:Int,acc:Int): Int ={
        if(a>b) acc
        else loop(a+1,f(a)+acc)
      }
      loop(a,0)
    }
/**
  * def sum(f:Int=>Int,a:Int,b:Int):Int={
      if(a>b) 0 else f(a) + sum(f,a+1,b)
    }
  * */
    def sumInt(a:Int,b:Int):Int = sum(x=>x,a,b)
    def sumCube(a:Int,b:Int):Int = sum(x=>x*x*x,a,b)
    def sumFactorial(a:Int,b:Int):Int = sum(factorial,a,b)
    def factorial(x:Int):Int = if(x==1) 1 else x+factorial(x-1)
    print(sumFactorial(1,2))
}
