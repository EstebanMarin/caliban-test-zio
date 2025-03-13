package com.orchestration.zioIssue

import zio.*
object Foo extends ZIOAppDefault {
  def works = ZIO.foreachParDiscard(1 to 1_000_000)(_ => ZIO.fail("Boom")).fold(_.toString, identity)

  def run = ZIO.foreachParDiscard(1 to 1_000_000)(_ => ZIO.fail("Boom")).foldCause(_.toString, identity) // SO error
}