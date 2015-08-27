package remoteActors

import akka.actor.{ActorLogging, Actor}
import prototype.exProtocol.{Response, Request}

// sending requests to the instance
class Hypervisor extends Actor with ActorLogging{

  //define remote actor to be connnected
  val remote =
    context.actorSelection( "akka.tcp://RemoteINSSystem@127.0.0.1:5150/user/novains" )

  def receive = {
    case "ping"=> {
      remote ! Request
    }
    case Response( quoteString ) => {
      log.info ( "Received Response from Nova instance" )
      log.info( s"Printing from Nova Hypervisor Actor: $quoteString" )
    }
  }

  /*
      postStop is being called when
      ActorSystem shuts down
   */
  override def postStop()= {
    log.info ( "Inside postStop method of Hypervisor Lifecycle Actor" )
  }

}
