//> using scala 3.6
//> using dep "dev.zio::zio:2.1.15"
//> using dep "dev.zio::zio-http:3.0.1"
//> using dep "com.github.ghostdogpr::caliban:2.9.1"
//> using dep "com.github.ghostdogpr::caliban-quick:2.9.1"

import zio.*
import zio.http.*
import caliban.*
import caliban.schema.Schema.auto.*
import caliban.schema.ArgBuilder.auto.*
import caliban.quick.* // adds extension methods to `api`

case class User(id: Int, name: String, username: Int)
case class Queries(
    users: Task[List[User]],
    userByIf: Int => Task[Option[User]]
)

object SimpleZIO extends ZIOAppDefault {

  val resolver = Queries(
    users = ZIO.succeed(List(User(1, "John", 1), User(2, "Jane", 2))),
    userByIf = (id: Int) => ZIO.succeed(Some(User(id, "John", 1)))
  )

  val api = graphQL(RootResolver(resolver))

  val runGraphQL = api.unsafe.runServer(
    port = 8080,
    apiPath = "/api/graphql",
    graphiqlPath = Some("/api/graphiql")
  )

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

  val app = Server.serve(routes).provide(configLayer, Server.live)

  def run =
    app *> ZIO.succeed(runGraphQL)
}
