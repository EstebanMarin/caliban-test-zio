package com.orchestration.calibanzio

import caliban.client.CalibanClientError.DecodingError
import caliban.client.FieldBuilder.*
import caliban.client.Operations.*
import caliban.client.SelectionBuilder.*
import caliban.client.__Value.*
import caliban.client.{ArgEncoder, Argument, ScalarDecoder, SelectionBuilder}

object Client:

  sealed trait Origin extends scala.Product with scala.Serializable
  object Origin {
    case object BELT extends Origin
    case object EARTH extends Origin
    case object MARS extends Origin

    given ScalarDecoder[Origin] =
      case __StringValue("BELT")  => Right(Origin.BELT)
      case __StringValue("EARTH") => Right(Origin.EARTH)
      case __StringValue("MARS")  => Right(Origin.MARS)
      case other => Left(DecodingError(s"Can't build Origin from input $other"))

    given ArgEncoder[Origin] =
      case Origin.BELT  => __EnumValue("BELT")
      case Origin.EARTH => __EnumValue("EARTH")
      case Origin.MARS  => __EnumValue("MARS")

  }

  type Engineer
  object Engineer {
    def shipName: SelectionBuilder[Engineer, String] =
      Field("shipName", Scalar())
  }

  type Character
  object Character {
    def name: SelectionBuilder[Character, String] = Field("name", Scalar())
    def nicknames: SelectionBuilder[Character, List[String]] =
      Field("nicknames", ListOf(Scalar()))
    def origin: SelectionBuilder[Character, Origin] = Field("origin", Scalar())
    def role[A](
        onCaptain: SelectionBuilder[Captain, A],
        onEngineer: SelectionBuilder[Engineer, A],
        onMechanic: SelectionBuilder[Mechanic, A],
        onPilot: SelectionBuilder[Pilot, A]
    ): SelectionBuilder[Character, Option[A]] =
      Field(
        "role",
        OptionOf(
          ChoiceOf(
            Map(
              "Captain" -> Obj(onCaptain),
              "Engineer" -> Obj(onEngineer),
              "Mechanic" -> Obj(onMechanic),
              "Pilot" -> Obj(onPilot)
            )
          )
        )
      )
  }

  type Pilot
  object Pilot {
    def shipName: SelectionBuilder[Pilot, String] = Field("shipName", Scalar())
  }

  type Mechanic
  object Mechanic {
    def shipName: SelectionBuilder[Mechanic, String] =
      Field("shipName", Scalar())
  }

  type Captain
  object Captain {
    def shipName: SelectionBuilder[Captain, String] =
      Field("shipName", Scalar())
  }

  type Queries = RootQuery
  object Queries {
    def characters[A](
        origin: Option[Origin] = None
    )(
        innerSelection: SelectionBuilder[Character, A]
    ): SelectionBuilder[RootQuery, List[A]] =
      Field(
        "characters",
        ListOf(Obj(innerSelection)),
        arguments = List(Argument("origin", origin, "Origin"))
      )
    def character[A](
        name: String
    )(
        innerSelection: SelectionBuilder[Character, A]
    ): SelectionBuilder[RootQuery, Option[A]] =
      Field(
        "character",
        OptionOf(Obj(innerSelection)),
        arguments = List(Argument("name", name, "String!"))
      )
  }

  type Mutations = RootMutation
  object Mutations {
    def deleteCharacter(name: String): SelectionBuilder[RootMutation, Boolean] =
      Field(
        "deleteCharacter",
        Scalar(),
        arguments = List(Argument("name", name, "String!"))
      )
  }
