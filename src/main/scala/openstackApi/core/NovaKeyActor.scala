package OpenstackActorsApi.core

import akka.actor.Actor
import com.woorea.openstack.nova.Nova
import openstackAuth.NovaConnector


/*
  We are using the Key-pair actor for
  our Key Service.
  <receive>: A Key name
  <return a Key object>
 */
class NovaKeyActor extends Actor{


  def connected(novaClient: Nova): Receive ={

    case key_name: String => {
      var found=false
      // Get Flavors and check if the flavor exists or not
      var keysSeq = scala.collection.JavaConversions.asScalaBuffer(NovaConnector.GetKeys(novaClient))
      for (key <- keysSeq) {
        if (key.getName==key_name){
          print(s"Found the Keypair $key_name")
          found=true
          //return the id to the caller
          sender ! key.getName
        }
      }
      if (!found){
        print(s"NOT found the Keypair $key_name")
        val key = None
        sender ! ""
      }
    }

  }

  def receive = {

    case novaClient: Nova => {

      println( "KeyActor ... Getting the Nova Client" )
      print(novaClient)
      context become connected( novaClient )

    }
  }

}
