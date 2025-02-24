package com.orchestration.federation

import caliban.quick._
import com.orchestration.federation.FederationData.characters.sampleCharacters
import com.orchestration.federation.FederationData.episodes.sampleEpisodes
import zio._

object FederatedApp extends ZIOAppDefault {

  val episodesApi   = FederatedApi.Episodes.api.runServer(8089, "/api/graphql", Some("/graphiql"))
  val charactersApi = FederatedApi.Characters.api.runServer(8088, "/api/graphql", Some("/graphiql"))

  override def run =
    (episodesApi zipPar charactersApi)
      .provide(
        EpisodeService.make(sampleEpisodes),
        CharacterService.make(sampleCharacters)
      )
}
