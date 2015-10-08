package OpenstackActorsApi.core

import OpenstackActorsApi.domain.{Get, ResultItem}
import OpenstackAuth._
import akka.actor.Actor
import akka.event.Logging
import com.woorea.openstack.nova.Nova


object NovaActor {

  //var instances = new ListBuffer[String]()
  var instances = NovaConnector
  var stresult : String = _
  //my nova client
  var client: Nova = _

  def getById(names: String) =  {

    //put the new instance in the attribute list
    // Success("deleted successfully")
//    instances+=names
    stresult=instances.flavor( "flavor-list" ).toString
    ResultItem(stresult)

  }

}

class NovaActor extends Actor {

  import NovaActor._
  val log = Logging(context.system, this)


  def receive = {
    case Get(names) => {
 //     sender ! getById(names)
      sender ! getById(names: String)

    }
 }

}