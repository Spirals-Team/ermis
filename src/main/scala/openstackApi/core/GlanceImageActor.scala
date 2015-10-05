package openstackApi.core

import akka.actor.Actor
import com.woorea.openstack.nova.Nova
import openstackAuth.NovaConnector


class GlanceImageActor extends Actor{


  def connected(novaClient: Nova): Receive ={


    case image_name: String => {
      // Search
      var found=false
      // Get Flavors and check if the flavor exists or  not
      var ImagessSeq = scala.collection.JavaConversions.asScalaBuffer(NovaConnector.GetImages(novaClient))
      for (image <- ImagessSeq) {
        if (image.getName==image_name){
          print(s"Found the requested Image $image_name")
          found=true
          //return the id to the caller
          sender ! image.getId
        }
      }
      if (!found){
      //  print(s"NOT found the requested Image $image_name")
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