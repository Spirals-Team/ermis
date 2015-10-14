package dsl

import java.io.IOException

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import dsl.NovaInstance

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try


object Requester {

  var request : HttpRequest =_

  /*
    main Request function
    params: request type
  */
  def Request ( inputRequest: String, novaInstance: NovaInstance ) ={

    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()

    val connection: Flow[HttpRequest, HttpResponse, Future[Http.OutgoingConnection]] =
      Http().outgoingConnection(host="localhost", port=7371)


    inputRequest match {

      case "connect" =>
        request = RequestBuilding.Get( s"/nova-demo" )
      case "instances" =>
        request = RequestBuilding.Get( s"/nova-demo/instances" )
      case "flavors" =>
        request = RequestBuilding.Get( s"/nova-demo/flavors" )
      case "create_instance" => {
        var query = "/nova-demo/create_instance?name=" + novaInstance._name + "&flavor=" + novaInstance._flavor + "&image=" + novaInstance._image + "&keyname=" + novaInstance._keyPair
        request = RequestBuilding.Get( query )
      }

    }

    Source.single( request ).via( connection ).runWith( Sink.head ).flatMap {

      response =>

        response.status match {

          case status if status.isSuccess => {
            val reply = Unmarshal( response.entity ).to[String]
            reply.onComplete {
              case str: Try[String] =>
                print( str.get.toString )
              case _ =>
                print("Could not connect to Nova Service")
            }
            reply
          }
          case status if status.isFailure() => {
            println( s"$status error:${response.toString}" )
            Future.failed( new IOException( s"Token request failed with status ${response.status} and error ${response.entity}" ) )
          }
        }
    }

  }

}