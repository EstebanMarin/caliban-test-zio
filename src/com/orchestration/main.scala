package com.orchestration

import zio.*

object HelloWorld extends ZIOAppDefault {
  def run =
    Console.printLine("Hello, World!")
}

