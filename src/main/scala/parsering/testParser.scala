package parsering

import scala.util.parsing.input.CharSequenceReader

object textParser extends DFParser{
  def main(args: Array[String]) = {
    parse(mainParser,new CharSequenceReader("在iris数据集上，按label汇总，求f1的最大值")) match {
      case Success(matched, _) => matched
      case _ => println("fail")
    }
  }
}