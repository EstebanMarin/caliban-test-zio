// > using scala 3.6
// > using dep "dev.zio::zio:2.1.15"
// > using dep "dev.zio::zio-http:3.0.1"
// > using dep "dev.zio::zio-logging:2.4.0"
// > using dep "dev.zio::zio-logging-slf4j2:2.4.0"
// > using dep "com.github.ghostdogpr::caliban:2.9.1"
// > using dep "com.github.ghostdogpr::caliban-quick:2.9.1"
// > using dep "org.slf4j:slf4j-simple:2.0.16"

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

