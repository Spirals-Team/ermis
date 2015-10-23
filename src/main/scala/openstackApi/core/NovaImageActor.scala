package openstackApi.core

import akka.actor.Actor
import com.woorea.openstack.nova.Nova
import openstackAuth.NovaConnector


class NovaImageActor extends Actor{


  def connected(novaClient: Nova): Receive ={

    case "images" => {

      sender ! NovaConnector.GetImages( novaClient )

    }
    case image_name: String => {
      var found=false
      // Get Flavors and check if the flavor exists or  not
      var ImagessSeq = scala.collection.JavaConversions.asScalaBuffer(NovaConnector.GetImages(novaClient))
      for (image <- ImagessSeq) {
        if (image.getName==image_name){
          print(s"Found Image $image_name\n")
          found=true
          //return the id to the caller
          sender ! image.getId
        }
      }
      if (!found){
        val image = None
        sender ! ""
      }
    }

  }

  def receive = {

    case novaClient: Nova => {

      println( "NovaImage ... Getting the Nova Client" )
      print(novaClient)
      context become connected( novaClient )

    }
  }

}