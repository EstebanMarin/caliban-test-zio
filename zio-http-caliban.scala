//> using scala 3.6
//> using dep "dev.zio::zio:2.1.15"
//> using dep "dev.zio::zio-http:3.0.1"
//> using dep "com.github.ghostdogpr::caliban:2.9.1"

import zio.*
import zio.http.*

object SimpleZIO extends ZIOAppDefault {

  val routes: Routes[Any, Response] =
    Routes(
      Method.GET / "hello" -> Handler.text("hello"),
      Method.GET / "health-check" -> Handler.ok,
      Method.POST / "echo" ->
        handler { (req: Request) =>
          req.body.asString.map(Response.text(_))
        }.sandbox
    )

  private val config = Server.Config.default
    .port(8090)

  private val configLayer = ZLayer.succeed(config)

  val serverConfig = Server.Config.default
//   val app = Server.serve(routes).provide(Server.defaultWithPort(8081))
//   val app = Server.serve(routes).provide(Server.defaultWithPort(8081))

  val app = Server.serve(routes).provide(configLayer, Server.live)

  def run =
    app
}
