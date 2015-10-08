package OpenstackActorsApi

import OpenstackActorsApi.api.RestRouting
import akka.actor.{Props, ActorSystem}
import akka.io.IO
import spray.can.Http

object Boot extends App {

  // create an ActorSystem
  implicit val system = ActorSystem( "nova-demo" )
  // create an Actor which serving Http requests
  val serviceActor = system.actorOf( Props( new RestRouting ), name = "rest-routing" )

  // time to shutdown the Actor serving a request
  system.registerOnTermination {
    system.log.info( "Actor per request demo shutdown." )
  }

  // Actor to a specific endpoint
  IO(Http) ! Http.Bind( serviceActor, "localhost", port = 7271 )

}