package parsering

import scala.util.parsing.combinator.Reg

//case class
//"在iris数据集上，按label汇总，求f1的最大值"
//"iris数据集，按label汇总，求f1的最大值"
case class DFGroupByMax(dfname:String,groupcol:String,aggcol:String){
  override def toString = "在<"+dfname+">数据集上，按<"+groupcol+">汇总，求<"+aggcol+">的最大值"
}
class DFParser extends RegexParsers{
  val _in: Parser[String] = "在"
  val _dfname: Parser[String] = """\w+""".r ^^ {_.toString}
  val _dataset: Parser[String] = "数据集上，按"
  val _groupcol: Parser[String] = """\w+""".r ^^ {_.toString}
  val _agg: Parser[String] = "汇总，求"
  val _aggcol: Parser[String] = """\w+""".r ^^ {_.toString}
  val _max: Parser[String] = "的最大值"
  val mainParser:Parser[DFGroupByMax] = _in ~ _dfname ~ _dataset ~ _groupcol ~ _agg ~ _aggcol ~ _max ^^ {
    case in ~ dfname ~ dataset ~ groupcol ~ agg ~ aggcol ~ max => DFGroupByMax(dfname,groupcol,aggcol)
  }
}

