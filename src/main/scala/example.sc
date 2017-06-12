class Rational(x: Int, y: Int){
  def numer = x
  def denom = y

  def add(r: Rational): Rational = {
    val numer_new = numer * r.denom + denom * r.numer
    val denom_new = denom * r.denom
    gcd(numer_new, denom_new)
  }

  def sub(r: Rational): Rational = {
    val numer_new = numer * r.denom - denom * r.numer
    val denom_new = denom * r.denom
    gcd(numer_new, denom_new)
  }

  def mul(r: Rational): Rational = {
    val numer_new = numer * r.numer
    val denom_new = denom * r.denom
    gcd(numer_new, denom_new)
  }

  def div(r: Rational): Rational = {
    val numer_new = numer * r.denom
    val denom_new = denom * r.numer
    if(denom_new==0) throw new IllegalArgumentException("0 cannot be divided")
    gcd(numer_new, denom_new)
  }

  override def toString: String = numer+"/"+denom

  def gcd(x: Int, y: Int): Rational = {
    var gcd = 1
    var a=x
    var b=y
    while (a != 0) {
      gcd = a
      a = b % a
      b = gcd
    }
    gcd = math.abs(gcd)
    new Rational(x/gcd,y/gcd)
  }
}
  val r = new Rational(1,2)
  val s = new Rational(0,3)
 r.div(s)
