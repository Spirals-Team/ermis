package openstackApi.api

import java.util
import openstackApi.core.NovaKeyActor
import akka.actor.{Actor, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.woorea.openstack.nova.Nova
import com.woorea.openstack.nova.model.{Flavor, Server}
import openstackApi.core._
import openstackApi.domain.CreateVMrq
import akka.http.scaladsl.server.Directives._
import openstackAuth.NovaConnector

import scala.concurrent.duration._


class AkkaHttpService(system: ActorSystem) extends Actor {

  implicit def actorRefFactory = context
  implicit val materializer = ActorMaterializer()
  var novaClient: Nova=_
  implicit val timeout = Timeout( 5 seconds )

  //Deploy Actors
  val FlavorActor = context.actorOf( Props[NovaFlavorActor], name="FlavorActor" )
  val KeyActor = context.actorOf( Props[NovaKeyActor], name="KeyActor" )
  val ImageActor = context.actorOf( Props[GlanceImageActor], name="ImageActor" )
  val NovaActor = context.actorOf( Props( classOf[NovaActor], FlavorActor, KeyActor, ImageActor) )
  val HypervisorActor = context.actorOf( Props[NovaHypervisorActor], name="HypervisorActor" )
  val InstanceActor = context.actorOf( Props[NovaInstanceActor], name="InstanceActor" )


  val route = {

    get {

      //Nova-Endpoint
      path( "nova-demo" ) {

        val Message = "Nova_Demo_Connect"

        onSuccess( NovaActor ? Message ) {
          case message: String => {
            complete( message )
          }
          case message: Nova => {
            FlavorActor ! message
            KeyActor ! message
            ImageActor ! message
            complete( "Connected to Nova Service" )
          }
        }
      }~
        //Create instance endpoint
        //The instance is created by the
        path( "nova-demo"/"create_instance" ) {
          parameters( 'name, 'flavor, 'image, 'keyname ) {
            ( name, flavor, image, keyname ) =>
              println( "Name: " + name + ", Flavor: " + flavor + ", Image: " + ", Keyname: " + keyname )

              val Message = CreateVMrq(name, flavor, image, keyname)

              onSuccess( NovaActor ? Message ) {
                case message: String => {
                  complete( message )
                }
                case server: Server => {
                  complete( s"Created new server with name: $name" )
                }
              }
          }
        }~
        path( "nova-demo"/"instances" ) {

          val Message = "Get_Instances"

          onSuccess( NovaActor ? Message ) {
            case message: util.List[Server] => {
              complete( NovaConnector.prettyPrint[Server](message) )            }
          }
        }~
        path( "nova-demo"/"flavors" ) {

          val Message = "flavors"

          onSuccess( FlavorActor ? Message ) {
            case message: util.List[Flavor] => {

              complete( NovaConnector.prettyPrint[Flavor](message) )

            }
          }
        }~
        path( "nova-demo"/"hosts"/Segment ) {

          hypervisor => {
            val Message = "Hyper_Connect"

            onSuccess( HypervisorActor ? Message ) {
              case message: String => {
                complete( message )
              }
            }
          }
        }~
        path( "nova-demo"/"hosts"/Segment/"instances"/Segment ) {
          ( _,instances ) => {

            val Message = "Instance_Connect"

            onSuccess( HypervisorActor ? Message ) {
              case message: String => {
                complete( message )
              }
            }
          }
        }

    }
  }

  Http(system).bindAndHandle( route, "localhost", 7371 )

  override def receive: Receive = {
    case str:String => context.stop( self )
  }

}