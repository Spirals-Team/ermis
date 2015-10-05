package openstackApi.api

import java.util

import OpenstackActorsApi.core.NovaKeyActor
import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.woorea.openstack.nova.Nova
import com.woorea.openstack.nova.model.Server
import dispatch.Future
import openstackApi.core._
import openstackApi.domain.CreateVMrq
import spray.routing._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Success}


class SprayRouteService extends HttpService with Actor {

  implicit def actorRefFactory = context
  def receive = runRoute(route)

  //create the respective Actors
  val FlavorActor = context.actorOf( Props[NovaFlavorActor], name="FlavorActor")
  val KeyActor = context.actorOf( Props[NovaKeyActor], name="KeyActor" )
  val ImageActor = context.actorOf( Props[GlanceImageActor], name="ImageActor" )

  val NovaActor = context.actorOf( Props(classOf[NovaActor], FlavorActor, KeyActor, ImageActor) )
  val HypervisorActor = context.actorOf( Props[NovaHypervisorActor], name="HypervisorActor" )
  val InstanceActor = context.actorOf( Props[NovaInstanceActor], name="InstanceActor" )

  //Nova Client returned
  var novaClient: Nova=_

  //Set the path Route
  //Spray Endpoint
  val route = {
    get {
      //Nova-Endpoint
      path("nova-demo") {

          onComplete(NovaConnect(NovaActor)) {

            case Success(value) => {
              //send the client to FlavorActor
              FlavorActor ! value
              KeyActor ! value
              ImageActor ! value
              //replie back to client with a message
              complete(s"The result was $value")
            }
            case Failure(value) =>
              complete("Could not connect OR Already Connected")
          }

      }~
      //Create instance endpoint
      //The instance is created by the
      path("nova-demo"/"create_instance") {
        parameters('name, 'flavor, 'image, 'keyname ) {
            (name, flavor, image, keyname) =>
            println("Name: "+name+", Flavor: "+flavor+", Image: "+", Keyname: "+keyname)
            onComplete( NovaCreateInstance( NovaActor, CreateVMrq(name, flavor, image, keyname ) ) ) {

              case Success(value) =>
                //reply back to client with a message
                complete(s"$value")

              case Failure(value) =>
                complete(s"$value")
            }
        }
      }~
      path("nova-demo"/"instances") {

        onComplete(NovaConnected(NovaActor)) {

          case Success(value) =>
            //reply back to client with a message
            complete(s"The result was $value")

          case Failure(value) =>
            complete("Not Connected to the model.\nFirst connect to the nova-demo")
        }

      }~
      //NovaHypervisor-Endpoint
      path("nova-demo"/"hosts"/Segment) {

        hypervisor =>
          onComplete(HypervisorConnect(HypervisorActor)) {
            //       case Success(value) => complete(s"The result was $value")
            case Success(value) => complete(s"The client was $value")
          }
      }~
      //NovaInstance-Endpoint
      path("nova-demo"/"hosts"/Segment/"instances"/Segment) {
          (_,instances) =>
            onComplete(InstanceConnect(InstanceActor)) {

              case Success(value) => complete(s"The result was $value")
            }
      }

    }
  }

  /*
    Connect to Nova Service
    - connects to nova and returns a Nova object
    - We will use the nova object for all the following connections
   */
  def NovaConnect(novaActor: ActorRef): Future[Nova] = {
    implicit val timeout = Timeout(5 seconds)
    if (novaClient!=null) {
      print("You are already connected")
    }
    val Message = "Nova_demo_Connect"
    (novaActor ? Message).mapTo[Nova]
  }

  def NovaConnected(novaActor: ActorRef): Future[util.List[Server]] = {
    implicit val timeout = Timeout(5 seconds)
    val Message = "Nova_instances_Connect"
    (novaActor ? Message).mapTo[util.List[Server]]
  }

  //Send request to Nova Actor
  //to create a new instance and add it to the existing list
  def NovaCreateInstance(novaActor: ActorRef, instancerq: CreateVMrq): Future[String] = {
    implicit val timeout = Timeout(5 seconds)
    val Message = instancerq
    (novaActor ? Message).mapTo[String]
  }
  /*
    Connect to a Hypervisor
   */
  def HypervisorConnect(hyperActor: ActorRef): Future[String] = {
    implicit val timeout = Timeout(5 seconds)
    val Message = "Hyper_Connect"
    (hyperActor ? Message).mapTo[String]
  }

  /*
    Connect to an Instance/VM
   */
  def InstanceConnect(instanceActor: ActorRef): Future[String] = {
    implicit val timeout = Timeout(5 seconds)
    val Message = "Instance_Connect"
    (instanceActor ? Message).mapTo[String]
  }

}