package OpenstackActorsApi2.api

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


object SprayRouting {
  //we will store nova client instance
  var client: Nova=_

}

class SprayRouting extends HttpService with Actor {

  implicit def actorRefFactory = context
  import OpenstackActorsApi.domain._

  def receive = runRoute(route)

  //create a new nova actor

  val novaActor = context.actorOf(Props[NovaActor], name = "myNovaActor")

  val route = {
    get {
      path("nova-demo" / Segment) {
        instances =>
          onComplete(getSummary(novaActor, instances)) {
    //       case Success(value) => complete(s"The result was $value")
            case Success(value) => complete(s"The result was $value")
          }
      }~
      path("nova-demo" / "hosts" / Segment) {

        instances =>
          onComplete(getSummary(novaActor, instances)) {
            //       case Success(value) => complete(s"The result was $value")
            case Success(value) => complete(s"The result was $value")
          }
      }~
      //Endpoint of the Instance Actor
      path("nova-demo" / "hosts" / Segment /"instances"/ Segment) {
          (_,instances) =>
            onComplete(getSummary(novaActor, instances)) {
              //       case Success(value) => complete(s"The result was $value")
              case Success(value) => complete(s"The result was $value")
            }
      }
    }
  }

  /*
    The getSummary function
   */
  def getSummary(novaActor: ActorRef, instances: String): Future[String] = {
    implicit val timeout = Timeout(5 seconds)
    val Message = Get("hello")
    (novaActor ? Message).mapTo[String]
  }

}