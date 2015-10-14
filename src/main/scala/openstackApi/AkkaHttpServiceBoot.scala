package openstackApi

import akka.actor.{ActorSystem, Props}
import openstackApi.api.AkkaHttpService


object AkkaHttpServiceBoot extends App {

  // create an ActorSystem
  implicit val system = ActorSystem( "AkkaHttpSystem" )
  // create an Actor which serving Http requests
  val serviceActor = system.actorOf( Props( new AkkaHttpService(system) ), name = "Akka-Http-Routing" )

  // time to shutdown the Actor serving a request
  system.registerOnTermination {
    system.log.info( "Actor per request demo shutdown." )
  }

}
