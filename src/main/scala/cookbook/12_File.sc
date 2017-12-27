case class Stock(var symbol:String,var price:BigDecimal) extends Serializable{
  override def toString: String = f"$symbol%s is ${price.toDouble}%.2f"
}

println(Stock("123",123))


import sys.process._

("ls -a -l"  #| "grep inspect.sh").!!

val process = Process("ls -a ").lines
process.foreach(println)

"pwd".!!.trim

"which hadoop".lines_!.headOption

"which ls".lines_!.headOption

val status = "ls -al log.xml" ! ProcessLogger(stdout append _,stderr append _)
status
println(stdout)
print(stderr)

Seq("/bin/sh","-c","ls | grep log").!!
Seq("/bin/sh","-c","ls log*").!!

