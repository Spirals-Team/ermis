package OpenstackActorsApi.core

import OpenstackActorsApi.domain.{Get, ResultItem}
import akka.actor.Actor
import akka.event.Logging

import scala.collection.mutable.ListBuffer


object NovaActor {

  var instances = new ListBuffer[String]()
  def getById(names: String) =  {

    //put the new instance in the attribute list
    // Success("deleted successfully")
    instances+=names
    ResultItem(instances.toList)

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