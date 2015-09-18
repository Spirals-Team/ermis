package OpenstackActorsApi.api

import OpenstackActorsApi.core.NovaActor
import OpenstackActorsApi.domain._
import akka.actor.{ActorSystem, Actor, Props}
import spray.routing.{HttpService, Route}

class RestRouting extends HttpService with Actor with PerRequestCreator {

  implicit def actorRefFactory = context

  def receive = runRoute(route)
  //create a new nova actor
  val NovaSystem=ActorSystem("instanceSystem")
  val instanceActor=NovaSystem.actorOf(Props[NovaActor], name = "myactor")

  val route = {
    get {
      //Endpoint of the Hypervisor Actor
      path("nova-demo"/ Segment){
        instances =>
          //handlePerRequest takes a Request as a parameter
          //Get(names)
          handlePerRequest {
            Get(instances)
          }
      }~
      path("nova-demo" / "hosts" / Segment) {

        names =>
          //handlePerRequest takes a Request as a parameter
          //Get(names)
          handlePerRequest {
            Get(names)
          }
      }~
      //Endpoint of the Instance Actor
      path("nova-demo" / "hosts" / Segment /"instances"/ Segment) {
        (_, names2) =>
          //handlePerRequest takes a Request as a parameter
          //Get(names)
          handlePerRequest {
            Get(names2)
          }
      }

    }

  }


  def handlePerRequest(message: RequestMessage): Route =
    ctx => perRequest(actorRefFactory, ctx, Props[NovaActor], message)

}
