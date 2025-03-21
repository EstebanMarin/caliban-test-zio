package com.orchestration.scalamacros

object MacrosFirst {

  case class Person(name: String, age: Int)

//   def string2Person(string: String): Person =
  // macro MacrosImpl.string2Person
  def string2Person(string: String): Person =
    val parts = string.split(",")
    Person(parts(0), parts(1).toInt)

  extension (string: StringContext)
    def per(args: Any*): Person =
      string2Person(string.parts.mkString)

  val esteban = per"Esteban,27"

  def main(args: Array[String]): Unit = {
    println(s"Person: $esteban")
  }
}
