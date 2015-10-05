package OpenstackActorsApi

import OpenstackActorsApi.api.SprayRouteService
import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http

object Boot extends App {

  // create an ActorSystem
  implicit val system = ActorSystem( "SprayActorSystem" )
  // create an Actor which serving Http requests
  val serviceActor = system.actorOf( Props( new SprayRouteService ), name = "Spray-rest-routing" )

  // time to shutdown the Actor serving a request
  system.registerOnTermination {
    system.log.info( "Actor per request demo shutdown." )
  }

  // Actor to a specific endpoint
  IO(Http) ! Http.Bind( serviceActor, "localhost", port = 7271 )

}