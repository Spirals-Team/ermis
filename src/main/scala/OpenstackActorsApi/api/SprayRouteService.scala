package OpenstackActorsApi.api

import OpenstackActorsApi.core.NovaActor
import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.woorea.openstack.nova.Nova
import dispatch.Future
import spray.routing._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.Success

//Companion Object
object SprayRouteService {
  //we will store nova client instance
  //in the beginning it's
  var client: Nova=_

}

class SprayRouteService extends HttpService with Actor {

  import SprayRouteService._
  implicit def actorRefFactory = context
  def receive = runRoute(route)

  val novaActor = context.actorOf(Props[NovaActor], name = "myNovaActor")

  val route = {
    get {
      //Nova-Endpoint
      path("nova-demo") {

          onComplete(NovaConnect(novaActor)) {
            case Success(value) =>
              client=value
              complete(s"The result was $value")
          }
      }~
      //NovaHypervisor-Endpoint
      path("nova-demo" / "hosts" / Segment) {

        hypervisor =>
          onComplete(HypervisorConnect(novaActor, client, hypervisor)) {
            //       case Success(value) => complete(s"The result was $value")
            case Success(value) => complete(s"The client was $value")
          }
      }~
      //NovaInstance-Endpoint
      path("nova-demo" / "hosts" / Segment /"instances"/ Segment) {
          (_,instances) =>
            onComplete(InstanceConnect(novaActor, instances)) {
              //       case Success(value) => complete(s"The result was $value")
              case Success(value) => complete(s"The result was nice")
            }
      }
    }
  }

  /*
    Connect()
    - connects to nova and returns a Nova object
    - We will use the nova object for all the following connections
   */
  def NovaConnect(novaActor: ActorRef): Future[Nova] = {
    implicit val timeout = Timeout(5 seconds)
    val Message = "hello"
    (novaActor ? Message).mapTo[Nova]
  }

  def HypervisorConnect(novaActor: ActorRef, novaClient: Nova, instances: String): Future[String] = {
    implicit val timeout = Timeout(5 seconds)
    val Message = novaClient
    (novaActor ? Message).mapTo[String]
  }

  def InstanceConnect(novaActor: ActorRef, instances: String): Future[Nova] = {
    implicit val timeout = Timeout(5 seconds)
    val Message = "hello"
    (novaActor ? Message).mapTo[Nova]
  }

}