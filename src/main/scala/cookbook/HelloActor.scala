package cookbook
import akka.actor.{Actor, ActorSystem, Props}

class HelloActor2(name:String) extends Actor{
  def receive: Receive = {
    case "Hello" => println("hello "+name)
    case _ => println("who?")
  }
}
class HelloActor extends Actor{
  override def receive: Receive = {
    case "Hello" => println("hello")
    case _ => println("who?")
  }
}
object Main extends App{
  val system = ActorSystem("HelloSystem")
  val helloActor = system.actorOf(Props[HelloActor],name="helloActor")
  helloActor ! "hello"
  helloActor ! "jax"
  val h2 = system.actorOf(Props(new HelloActor2("jax")),name = "h1")
  h2 ! "hello"
}
