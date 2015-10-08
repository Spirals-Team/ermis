package openstackApi.core

import akka.actor.Actor


class NovaInstanceActor extends Actor{

  def receive = {
    case "Instance_Connect" => {
      println("Instance Connection")
      sender ! "Hi from Instance"
    }
  }

}
