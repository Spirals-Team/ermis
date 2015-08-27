package remoteActors

import akka.actor.{ActorLogging, Actor}
import prototype.exProtocol.{Response, Request}
import scala.util.Random

//listening for requests
class Instance extends Actor with ActorLogging{

  val quotes = List(
    "Moderation is bad",
    "Anything worth doing is worth overdoing",
    "The trouble is you think you have time",
    "You never gonna know if you never even try" )

  def receive = {

    case Request => {
      println( "Got the request" )
      //Get a random Quote from the list and construct a response
      val inResponse = Response( quotes( Random.nextInt( quotes.size ) ) )

      //respond back to the Student who is the original sender of QuoteRequest
      sender ! inResponse

    }

  }

  override def postStop()= {
    log.info ( "Inside postStop method of Instance Lifecycle Actor" )
  }

}

