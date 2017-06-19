def last1[ T ](xs: List[ T ]): T = xs match {
  case List() => throw new Error("last of empty list")
  case List(x) => x
  case y :: ys => last1(ys)
}
def init1[ T ](xs: List[ T ]): List[ T ] = xs match {
  case List() => throw new Error("init of empty list")
  case List(x) => List()
  case y :: ys => xs take (xs.length - 1)
}
var x = List(9, 8, 7, 6, 5, 4, 3, 2, 1)
var y = List(19, 18, 17, 16, 15, 14, 13, 12, 11)
last1(x)
init1(x)

def concat1[ T ](xs: List[ T ], ys: List[ T ]): List[ T ] = xs match {
  case List() => ys
  case z :: zs => z :: concat1(zs, ys)
}
def reverse1[ T ](xs: List[ T ]): List[ T ] = xs match {
  case List() => xs
  case z :: zs => reverse1(zs) ++ List(z)
}
reverse1(x)
def removeAt1[ T ](n: Int, xs: List[ T ]): List[ T ] = xs match {
  case List() => xs
  case z :: zs => if (n < 0 || n >= xs.length) xs else (xs take n) ++ (xs drop (n + 1))
}
removeAt1(3, x)




def isort(list: List[ Int ]): List[ Int ] = list match {
  case List() => List()
  case x :: xs => insert(x, isort(xs))
}
def insert(x: Int, xs: List[ Int ]): List[ Int ] = xs match {
  case List() => List(x)
  case y :: ys => if (x <= y) x :: xs else y :: insert(x, ys)
}
def msort1[T](xs: List[ T ])(lt:(T,T)=>Boolean): List[ T ] = {
  val n = xs.length / 2
  if (n == 0) xs
  else {
    //    def merge(ys: List[ Int ], zs: List[ Int ]): List[ Int ] = ys match {
    //      case Nil => zs
    //      case y1 :: yn =>{
    //        zs match{
    //          case Nil=> ys
    //          case z1::zn=>if(y1<z1) y1::merge(yn,zs) else z1::merge(ys,zn)
    //        }
    //      }
    //    }
    def merge(ys: List[ T ], zs: List[ T ]): List[ T ] = (ys, zs) match {
      case (Nil, zs) => zs
      case (ys, Nil) => ys
      case (y1 :: yn, z1 :: zn) => if (lt(y1, z1)) y1 :: merge(yn, zs) else z1 :: merge(ys, zn)
    }
    val (fst, snd) = xs splitAt n
    merge(msort1(fst)(lt), msort1(snd)(lt))
  }
}
msort1(x)((a,b)=>a<b)
var letter = List("b","c","e","s","h")
msort1(letter)((x,y)=>x.compareTo(y)<0)
def msort2[T](xs: List[ T ])(implicit ord:Ordering[T]): List[ T ] = {
  val n = xs.length / 2
  if (n == 0) xs
  else {
    def merge(ys: List[ T ], zs: List[ T ]): List[ T ] = (ys, zs) match {
      case (Nil, zs) => zs
      case (ys, Nil) => ys
      case (y1 :: yn, z1 :: zn) => if (ord.lt(y1, z1)) y1 :: merge(yn, zs) else z1 :: merge(ys, zn)
    }
    val (fst, snd) = xs splitAt n
    merge(msort2(fst), msort2(snd))
  }
}
msort2(letter)
var fruit = List[ Int ](2, 3, 1)
print(isort(fruit))

//List Methods

x.length
x.head
x.tail
x.init
x.last
x take 5
x drop 5
x(5)
x(0)
x contains 3
x ++ y
(x ++ y).reverse
x updated(4, 4)
x indexOf 5
x filter (x=> x>3)
x filter (_>3)
x filterNot (_>3)
x partition(_>4)

def pack1[T](xs:List[T]):List[List[T]]= xs match {
  case Nil =>Nil
  case y::ys => {
    val (first,rest) = xs span(_==y)
    first::pack1(rest)
  }
}
def encode1[T](xs:List[T]):List[List[ Any ] ]= xs match {
  case Nil =>Nil
  case y::ys => {
    val (first,rest) = xs span(_==y)
    List(y,first.length)::encode1(rest)
  }
}
val data = List("a","a","a","b","c","c","a")
pack1(data)
encode1(data)

def sum(xs:List[Int]) = (0::xs) reduceLeft ((x,y)=>x+y)
def product(xs:List[Int]) = (1::xs) reduceLeft ((x,y)=>x*y)
def sum1(xs:List[Int]) = (0::xs) reduceLeft (_+_)
def product1(xs:List[Int]) = (1::xs) reduceLeft (_*_)
def sum2(xs:List[Int]) = (xs foldLeft 0) (_+_)
def product2(xs:List[Int]) = (xs foldLeft 1) (_*_)
sum(x)
product(x)

def concat[T](xs:List[T],ys:List[T]):List[T]=(xs foldRight ys)(_::_)
def reverse2[T](xs:List[T]):List[T]=(xs foldLeft List[T]())((xs,x)=>x::xs)
reverse2(x)