//> using options "-Xprint:postInlining"
//> using options "Xmax-inlines=1000"

package com.orchestration.scalamacros

object SimpleInlines {

  def increment: Int => Int = _ + 1
  inline def inc: Int => Int = _ + 1

  val three = 3
  val four = inc(three)

  val otherFour = increment(three)

  val eight = inc(2 * three + 1)

  inline def incia(inline x: Int): Int = x + 1

  val nine = incia(3 * three + 1)

  inline def condition(b: Boolean): String =
    inline if b then "It's true!" else "It's false!"

  val positive = condition(true)

  transparent inline def condition2(b: Boolean): String |  Int =
    inline if b then "It's true!" else 42

  val positive2 = condition2(true) // compile time know String
  val negative2 = condition2(false) // compile time know Int

  def main(args: Array[String]): Unit = {
    println(s"Simple Inline")
  }
}
