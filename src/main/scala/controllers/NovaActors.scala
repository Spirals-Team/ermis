package controllers

import scala.util.Random
import akka.actor._
import prototype.exProtocol._


class NovaHypervisor (NovaInstanceRef:ActorRef) extends Actor with ActorLogging{


    def receive = {
        case 'InitSignal=> {
            NovaInstanceRef ! Request
        }
        case Response(quoteString) => {
            log.info ("Received Response from Nova instance")
            log.info(s"Printing from Nova Hypervisor Actor: $quoteString")
        }
    }
    /*
        postStop is being called when
        ActorSystem shuts down
     */
    override def postStop()={
        log.info ("Inside postStop method of NovaHypervisor Lifecycle Actor")
    }
}

class NovaInstance extends Actor with ActorLogging{

    val quotes = List(
        "Moderation is bad",
        "Anything worth doing is worth overdoing",
        "The trouble is you think you have time",
        "You never gonna know if you never even try")

    def receive = {

        case Request => {
            println("hallo welt")
            //Get a random Quote from the list and construct a response
            val inResponse = Response(quotes(Random.nextInt(quotes.size)))

            //respond back to the Student who is the original sender of QuoteRequest
            sender ! inResponse

        }
    }
    override def postStop()={
        log.info ("Inside postStop method of NovaInstance Lifecycle Actor")
    }
}


object HowtoAkka extends App {

    val system = ActorSystem("NovaSystem")
    val insref = system.actorOf(Props[NovaInstance], "novains")
    val hpvref = system.actorOf(Props(new NovaHypervisor(insref)), "novahpv")

    println (insref.path)
    println (hpvref.path)
    //send message to hpvref
    hpvref ! 'InitSignal
    //shut down the actor system
    system.shutdown()
}