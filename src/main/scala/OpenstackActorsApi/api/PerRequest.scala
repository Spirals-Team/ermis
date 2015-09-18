package OpenstackActorsApi.api

import OpenstackActorsApi.api.PerRequest.WithProps
import OpenstackActorsApi.domain
import OpenstackActorsApi.domain._
import akka.actor.SupervisorStrategy.Stop
import akka.actor.{OneForOneStrategy, _}
import org.json4s.DefaultFormats
import spray.http.HttpHeaders.Location
import spray.http.StatusCodes._
import spray.http.{HttpHeader, StatusCode}
import spray.httpx.Json4sSupport
import spray.routing.RequestContext

import scala.concurrent.duration._



trait PerRequest extends Actor with Json4sSupport{
  def r: RequestContext
  def target: ActorRef
  def message: RequestMessage

  import context._

  val json4sFormats = DefaultFormats

  setReceiveTimeout(2.seconds)

  target ! message

  def receive = {

    case domain.Created(location) => complete(spray.http.StatusCodes.Created, "",  List(new Location(location)))
    case ResultItem(items) => complete(OK, items)
    case domain.Success2(message) => complete(OK, message)
    case ReceiveTimeout => complete(GatewayTimeout, "Request timeout")

  }

  def complete[T <: AnyRef](status: StatusCode, obj: T, headers: List[HttpHeader] = List()) = {
    //r.complete(obj)
   r.withHttpResponseHeadersMapped(oldheaders => oldheaders:::headers).complete(status, obj)
   // stop(self)
  }

  override val supervisorStrategy =
    OneForOneStrategy() {
      case e => {
        complete(InternalServerError, Error(e.getMessage))
        Stop
      }
    }
}

object PerRequest {
  case class WithProps(r: RequestContext, props: Props, message: RequestMessage) extends PerRequest {
    //create a Hypervisor actor
    // Actors are created by using a Props instance into the actorOf
    // factory method which is available on ActorSystem and ActorContext
    lazy val target = context.actorOf(props)
  }

}

trait PerRequestCreator {
  def perRequest(actorRefFactory: ActorRefFactory, r: RequestContext, props: Props, message: RequestMessage) =
    actorRefFactory.actorOf(Props(new WithProps(r, props, message)))
}
