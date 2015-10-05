package openstackApi.core

import akka.actor.Actor


class NovaHypervisorActor extends Actor{

  def receive = {
    case "Hyper_Connect" => {
      println("Hypervisor Connection")
      sender ! "Hi from Hypervisor"
    }
  }

}
