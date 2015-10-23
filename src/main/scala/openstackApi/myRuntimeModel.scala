package openstackApi


import java.util

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.woorea.openstack.glance.model.Image
import com.woorea.openstack.nova.Nova
import com.woorea.openstack.nova.model.{Flavor, KeyPair, Server}
import dsl.NovaInstance
import openstackApi.core.NovaKeyActor
import openstackApi.core._
import openstackApi.domain.CreateVMrq
import openstackAuth.NovaConnector

import scala.concurrent.Await
import scala.concurrent.duration._



object myRuntimeModel {

  implicit val timeout = Timeout(5 seconds)

  implicit class novaInstanceHandler(val source: ActorRef) {


    def +=( instance: NovaInstance) ={

      val Message = CreateVMrq(instance._name, instance._flavor, instance._image, instance._keyPair)
      var futureNovaClient = NovaActor ? Message
      var resultNovaClient = Await.result(futureNovaClient, timeout.duration)

      resultNovaClient match    {

        case result: Server =>
          print("Created new server with name: " + instance)
      }

    }

    def apply() ={
      var futureNovaClient = NovaActor ? "Get_Instances"
      var resultNovaClient = Await.result(futureNovaClient, timeout.duration)

      resultNovaClient match {

        case message: util.List[Server] =>
          print(NovaConnector.prettyPrint[Server](message))
      }
    }

  }

  var novaCl : Nova = _

  implicit val system = ActorSystem( "sampleActorSystem" )

  //Deploy Actors
  val FlavorActor = system.actorOf( Props[NovaFlavorActor], name="flavorActor" )
  val KeyActor = system.actorOf( Props[NovaKeyActor], name="keyActor" )
  val ImageActor = system.actorOf( Props[NovaImageActor], name="imageActor" )
  val NovaActor = system.actorOf( Props( classOf[NovaActor], FlavorActor, KeyActor, ImageActor), name="novaActor" )
  val HypervisorActor = system.actorOf( Props[NovaHypervisorActor], name="hypervisorActor" )
  val InstanceActor = system.actorOf( Props[NovaInstanceActor], name="instanceActor" )

  //check the subclass above
  var instances = novaInstanceHandler(NovaActor)

  /**
   * connect to Nova Model
   */
  def connect() ={

    var futureNovaClient = NovaActor ? "Nova_Demo_Connect"
    var resultNovaClient = Await.result(futureNovaClient, timeout.duration)

    //also use tell and send the client

    resultNovaClient match {

      case result: String =>
        print(result)

      case result: Nova =>
        FlavorActor ! result
        KeyActor ! result
        ImageActor ! result
        print("Connected to Nova Model")
        novaCl = resultNovaClient.asInstanceOf[Nova]

    }
  }


  def flavors() = {

    var futureNovaClient = FlavorActor ? "flavors"
    var resultNovaClient = Await.result(futureNovaClient, timeout.duration)

    resultNovaClient match {

      case message: util.List[Flavor] =>
        print(NovaConnector.prettyPrint[Flavor](message))

    }
  }

  def images() = {

    var futureNovaClient = ImageActor ? "images"
    var resultNovaClient = Await.result(futureNovaClient, timeout.duration)

    resultNovaClient match {

      case message: util.List[Image] =>
        print(NovaConnector.prettyPrint[Image](message))

    }
  }

  def keyPairs() = {

    var futureNovaClient = KeyActor ? "keyPairs"
    var resultNovaClient = Await.result(futureNovaClient, timeout.duration)

    resultNovaClient match {

      case message: util.List[KeyPair] =>
        print(NovaConnector.prettyPrint[KeyPair](message))

    }
  }

}

