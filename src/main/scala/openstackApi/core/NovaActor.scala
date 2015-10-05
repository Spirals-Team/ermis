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

  /*
   *@param: NovaClient instance
   *@return: The new instance
   */
  def create_instance( novaClient: Nova, instanceinfo: CreateVMrq ): Server = {

    //create a new instance
    val serverForCreate = new ServerForCreate()
    serverForCreate.setName( instanceinfo.name )

    implicit val timeout = Timeout(5 seconds)

    //check the requested Flavor
    val futureFlavor = FlavorActor ? "dlyse.small"
    val resultFlavor = Await.result(futureFlavor, timeout.duration).asInstanceOf[String]
    if (!resultFlavor.isEmpty) {
      serverForCreate.setFlavorRef(resultFlavor)
    }
    else
      return null

    //for the images ask the GlanceImageActor
    val futureImage = ImageActor ? "Ubuntu 14.04.2 LTX"
    val resultImage = Await.result(futureImage, timeout.duration).asInstanceOf[String]
    if (!resultImage.isEmpty) {
      serverForCreate.setImageRef(resultImage)
    }
    else
      return null

    //erverForCreate.setImageRef( novaClient.images().list( true ).execute().getList.get(0).getId() )
    //check the requested Keypair
    val futureKey = KeyActor ? "lalos"
    val resultKey = Await.result(futureKey, timeout.duration).asInstanceOf[String]
    if (!resultKey.isEmpty) {
      serverForCreate.setKeyName(resultKey)
    }
    else
      return null

    //create the new server -- create nova
    val server = novaClient.servers().boot(serverForCreate).execute()
    print(server.toString)

    return server

  }

  //State_1
  //We have already created the nova client
  //Return instances list
  def is_connected( client: Nova, servers: util.List[Server] ): Receive = {

    case "Nova_instances_Connect" => {

      println( "I am already Connected, get my VMs" )


      sender ! servers
      context become is_connected( client, servers )

    }
    /*
    in that case: we create a new instance
    and return it to the sender
     */
    case instancerq: CreateVMrq => {

      print("Let's create a new instance")
      var server=create_instance( client, instancerq )
      if (server != null) {
        sender ! server
        servers.add(server)
      }
      else
        sender ! server
      context become is_connected( client,servers )

    }

  }

  //State_0
  //First time: We connect to Nova Service
  //Create a Nova Client, return it to the parent
  def receive = {

    case "Nova_demo_Connect" => {

      println("Setting up the Nova Connection")
      var client= NovaConnector.getNovaClient()
      var servers = NovaConnector.GetServers(client)
      sender ! client
      context become is_connected(client, servers)

    }

  }

}