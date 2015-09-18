package OpenstackActorsApi.core

import java.util

import OpenstackActorsApi.domain.{Get, ResultItem}
import OpenstackAuth._
import akka.actor.Actor
import akka.event.Logging
import com.woorea.openstack.nova.Nova
import com.woorea.openstack.nova.model.Flavor

object NovaActor {

  var flavor_list: util.List[Flavor]=_
  //var instances = new ListBuffer[String]()
  var instances = NovaConnector
  var stresult : String = _
  //my nova client
  var client: Nova = instances.getNovaClient()


  def getById(names: String) =  {

    flavor_list=instances.GetFlavors( client )
    var x=flavor_list
   // print( x)
    ResultItem(stresult)

  }

}
class NovaActor extends Actor {
  import NovaActor._
  val log = Logging(context.system, this)

  def advanced(names: String): Receive = {
    case Get(names) => {
      print("Am indeed advanced")
  //     sender ! getById(names)
      sender ! getById(names)
    }

  }

  def receive = {

    case Get(names) => {
      print("Am not advanced")
      sender ! names
   //     context.become(advanced(names))
    }
 }

}