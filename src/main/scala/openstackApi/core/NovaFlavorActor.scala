package openstackApi.core

import openstackAuth._
import akka.actor.Actor
import com.woorea.openstack.nova.Nova


/*
  We are using the Flavor actor for
  our Flavor Service.
  <receive>: A flavor name
  <return a Flavor object>
 */
class NovaFlavorActor extends Actor{

  def connected(novaClient: Nova): Receive ={

    case flavor_name: String => {
      var found=false
      // Get Flavors and check if the flavor exists or not
      var FlavorsSeq = scala.collection.JavaConversions.asScalaBuffer(NovaConnector.GetFlavors(novaClient))
      for (flavor <- FlavorsSeq) {
        if (flavor.getName==flavor_name){
          print(s"Found the flavor $flavor_name")
          found=true
          //return the id to the caller
          sender ! flavor.getId
        }
      }
      if (!found){
          print(s"NOT found the flavor $flavor_name")
          val flavor = None
          sender ! ""
      }
    }

  }

  def receive = {

    case novaClient: Nova => {

      println( "NovaFlavor ... Getting the Nova Client" )
      print(novaClient)
      context become connected( novaClient )

    }
  }

}
