package com.orchestration.calibanzio

import com.orchestration.calibanzio.Client.*
import com.orchestration.calibanzio.Client.Character.*
import caliban.client.CalibanClientError
import sttp.capabilities.WebSockets
import sttp.capabilities.zio.ZioStreams
import sttp.client3._
import sttp.client3.httpclient.zio.HttpClientZioBackend
import zio.Console.printLine
import zio.*
import izumi.reflect.Tag

object ExampleAppItzumi extends ZIOAppDefault {

  sealed trait RoleIt
  object RoleIt {
    case class Captain(shipName: String) extends RoleIt
    case class Pilot(shipName: String) extends RoleIt
    case class Engineer(shipName: String) extends RoleIt
    case class Mechanic(shipName: String) extends RoleIt
  }

  case class Character(
      name: String,
      nicknames: List[String],
      origin: Origin,
      role: Option[RoleIt]
  )

  def run = {
    val character = {
      // import example.client.Client.Character._
      // import org.orchestration.calibanzio.Client.Character.*
      (name ~
        nicknames ~
        origin ~
        role(
          Captain.shipName.map(RoleIt.Captain.apply),
          Engineer.shipName.map(RoleIt.Engineer.apply),
          Mechanic.shipName.map(RoleIt.Mechanic.apply),
          Pilot.shipName.map(RoleIt.Pilot.apply)
        )).mapN(Character.apply)
    }
    val query =
      Queries.characters(None) {
        character
      } ~
        Queries.character("Amos Burton") {
          character
        } ~
        Queries.character("Naomi Nagata") {
          character
        } ~
        Queries.character("Alex Kamal") {
          character
        }
    val mutation = Mutations.deleteCharacter("James Holden")

    def sendRequest[T: Tag](
        req: Request[Either[CalibanClientError, T], Any]
    ): RIO[SttpBackend[Task, ZioStreams & WebSockets], T] =
      ZIO
        .serviceWithZIO[SttpBackend[Task, ZioStreams & WebSockets]](req.send(_))
        .map(_.body)
        .absolve
        .tap(res => printLine(s"Result: $res"))

    val uri = uri"http://localhost:8088/api/graphql"
    val call1 = sendRequest(mutation.toRequest(uri))
    val call2 = sendRequest(query.toRequest(uri, useVariables = true))

    (call1 *> call2).provideLayer(HttpClientZioBackend.layer())
  }
}
