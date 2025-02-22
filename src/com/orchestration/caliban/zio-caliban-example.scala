package com.orchestration

import zio.*
import zio.logging.*
import zio.http.*


object ZioCalibanExample extends ZIOAppDefault {
  def run =
    for {
      _ <- Console.printLine("Hello")
      _ <- ZIO.logInfo("❤️")
      // httpFiber <- app
      // _ <- graphQLFiber.join
      // _ <- httpFiber.join
    } yield ()
}

