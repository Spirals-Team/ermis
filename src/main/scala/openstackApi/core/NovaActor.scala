package openstackApi.core

import java.util
import openstackAuth._
import akka.actor.{Actor, ActorRef}
import akka.event.Logging
import akka.pattern.ask
import akka.util.Timeout

import com.woorea.openstack.nova.Nova
import com.woorea.openstack.nova.model.{Server, ServerForCreate}
import openstackApi.domain.CreateVMrq

import scala.concurrent.Await
import scala.concurrent.duration._



//i don't need a companion object
class NovaActor(FlavorActor: ActorRef, KeyActor: ActorRef, ImageActor: ActorRef) extends Actor {

  val log = Logging(context.system, this)

  //State_1
  //We have already created the nova client
  //Return instances list
  def is_connected( client: Nova, servers: util.List[Server] ): Receive = {

    case "Nova_Demo_Connect" => {
      sender ! "You are already connected to Nova"
    }

    case "Get_Instances" => {

      sender ! NovaConnector.GetServers(client)
      context become is_connected( client, servers )

    }
    /*
    in that case: we create a new instance
    and return it to the sender
     */
    case instanceinfo: CreateVMrq => {

      //create a new instance
      val serverForCreate = new ServerForCreate()
      serverForCreate.setName( instanceinfo.name )

      implicit val timeout = Timeout(5 seconds)

      //check the requested Flavor
      val futureFlavor = FlavorActor ? instanceinfo.flavor
      val resultFlavor = Await.result(futureFlavor, timeout.duration).asInstanceOf[String]
      if (!resultFlavor.isEmpty) {
        serverForCreate.setFlavorRef(resultFlavor)
      }
      else
        sender ! "Flavor not Found"

      //for the images ask the GlanceImageActor
      val futureImage = ImageActor ? instanceinfo.image
      val resultImage = Await.result(futureImage, timeout.duration).asInstanceOf[String]
      if (!resultImage.isEmpty) {
        serverForCreate.setImageRef(resultImage)
      }
      else
        sender ! "Image not Found"

      val futureKey = KeyActor ? instanceinfo.keyname
      val resultKey = Await.result(futureKey, timeout.duration).asInstanceOf[String]
      if (!resultKey.isEmpty) {
        serverForCreate.setKeyName(resultKey)
      }
      else
        sender ! "KeyPAIR not Found"

      //create the new server -- create nova
      if (!resultFlavor.isEmpty && !resultImage.isEmpty && !resultKey.isEmpty) {
        val server = client.servers().boot(serverForCreate).execute()
        sender ! s"Created server with name $server"
        servers.add(server)
      }

      context become is_connected( client,servers )

    }

  }

  /*
    Failure Case:
      If we fail connecting to Nova Service using getNovaClient()
      we throw the exception. The Nova actor catches the exception and
      re-throws it. Sends back to the Spray Actor the cause of the error

    *State_0
    First time: We connect to Nova Service
    Create a Nova Client, return it to the parent
   */
  def receive = {

    case "Nova_Demo_Connect" => {

      println("Setting up the Nova Connection")
      //handke the case of exception
      try {
        var client = NovaConnector.getNovaClient()
        var servers = NovaConnector.GetServers(client)
        sender ! client
        context become is_connected(client, servers)

      } catch {

          case e: Exception ⇒
            print("XAXAXAXAX" + e)
            sender ! akka.actor.Status.Failure(e)
            throw e
      }
    }


  }

}