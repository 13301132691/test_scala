val capitalOfCountry = Map("US" -> "Washington", "Switzerland" -> "Bern")
val cap = capitalOfCountry withDefaultValue "<unknown>"
val countryOdCapital = capitalOfCountry map { case (x, y) => (y, x) }
cap("bj")
capitalOfCountry get "bj"
def showCapital(country: String): String = capitalOfCountry.get(country) match {
  case Some(capital) => capital
  case None => "missing data"
}

val fruit = List("apple","pear","orange","pineapple")
fruit sortWith(_.length<_.length)
fruit.sorted
fruit groupBy(_.head)
fruit.grouped(4)