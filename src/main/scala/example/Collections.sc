val nums = Vector(1, 2, 3, 4, 6, 7, 5, 8, 9)
nums :+ 1
1 +: nums
nums +: nums

val xs: Array[ Int ] = Array(1, 2, 3, 4, 5)
xs map (_ * 2)
val ys: String = "Hello world"
ys filter (_.isUpper)
1 to 10 by 2
(1 to 2) flatMap (x => (1 to 2) map (y => (x, y)))
def scalarProduct[ T ](xs: Vector[ T ], ys: Vector[ T ])(acc: (T, T) => T): Vector[ T ] = {
  (xs zip ys).map { case (x, y) => acc(x, y) }
}
scalarProduct(nums, nums)((x, y) => x * y).sum
def scalarProduct2[ T ](xs: Vector[ T ], ys: Vector[ T ])(acc: (T, T) => T): Vector[ T ] = {
  for ((x, y) <- xs zip ys) yield acc(x, y)
}
scalarProduct2(nums, nums)((x, y) => x * y).sum
def isPrime(n: Int): Boolean = (2 until n) forall (n % _ != 0)
isPrime(5)
def sumPrime(n: Int) = ((1 to n) flatMap (i => (1 to i) map (j => (i, j)))) filter (pair => isPrime(pair._1 + pair._2))
sumPrime(7)
def sumPrime2(n: Int) = for {
  i <- 1 to n
  j <- 1 to i
  if isPrime(i + j)
} yield (i, j)
sumPrime2(7)
