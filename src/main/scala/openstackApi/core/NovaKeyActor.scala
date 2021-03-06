package openstackApi.core

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

    case "keyPairs" => {

      sender ! NovaConnector.GetKeys( novaClient )

    }
    case key_name: String => {
      var found=false
      // Get Flavors and check if the flavor exists or not
      var keysSeq = scala.collection.JavaConversions.asScalaBuffer(NovaConnector.GetKeys(novaClient))
      for (key <- keysSeq) {
        if (key.getName==key_name){
          print(s"Found Keypair $key_name\n")
          found=true
          //return the id to the caller
          sender ! key.getName
        }
      }
      if (!found){
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
