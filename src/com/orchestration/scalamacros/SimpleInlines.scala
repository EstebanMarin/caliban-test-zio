//> using options "-Xprint:postInlining"
//> using options "Xmax-inlines=1000"

package com.orchestration.scalamacros

object SimpleInlines {

  def increment(x: Int): Int = x + 1
  inline def inc(x:Int): Int = increment(x)

  val number = 3 
  val four = inc(number)
  
  def main(args: Array[String]): Unit = {
    println(s"Simple Inline")
  }
}
