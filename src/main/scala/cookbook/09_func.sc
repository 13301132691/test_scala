class Foo{
  def exec(f:String=>Unit,name:String): Unit ={
    f(name)
  }
}
var hi = "hi"
def sayHi(name:String) = println(s"$hi "+name)

val foo = new Foo
foo.exec(sayHi,"jax")

hi = "Hello"
foo.exec(sayHi,"jax")

var votingAge = 20

def isVotingAge(age:Int) = age > votingAge

def printResult(f:Int=>Boolean,i:Int): Unit ={
  print(f(i))
}

printResult(isVotingAge,21)

val sum = (a:Int,b:Int,c:Int) => a + b + c
val f = sum(1,2,_:Int)
f(4)

def greeting(language:String) = (name:String) => {
  language match {
    case "english" => "Hello " + name
    case "chinese" => "你好 " + name
  }
}
val greetCN = greeting("chinese")
greetCN("jax")

val divide:PartialFunction[Int,Int] = {
  case d:Int if d!=0 => 42/d
}

val divide2 = new PartialFunction[Int,Int] {
  def apply(x:Int) = 27/x
  override def isDefinedAt(x: Int): Boolean = x != 0
}

if(divide.isDefinedAt(1)){
  divide(1)
}



val divide3 = divide orElse divide

val isEven:PartialFunction[Int,String] = {
  case x if x%2==0 =>x + " is Even"
}

val isOdd:PartialFunction[Int,String] = {
  case x if x%2==1 =>x + " is Odd"
}

val sample = 1 to 1

val numbers = sample collect (isEven orElse isOdd)
