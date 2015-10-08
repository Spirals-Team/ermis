package dsl

import java.io.IOException

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl._
import com.woorea.openstack.nova.Nova

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


object tester2 {

  var novaClient: Nova=_

  def Request (host:String, port:Int) ={

    implicit val system = ActorSystem()
    implicit val materializer = ActorFlowMaterializer()

    val connection: Flow[HttpRequest, HttpResponse, Future[Http.OutgoingConnection]] =
      Http().outgoingConnection(host="localhost", port = 7371)
    //   Http().outgoingConnection("http://akka.io")

    val request:HttpRequest = RequestBuilding.Get(s"/nova-demo")

    Source.single(request).via(connection).runWith(Sink.head).flatMap {

      response =>
      response.status match {

        case status if status.isSuccess => {
          val reply = Unmarshal(response.entity).to[String]
          println(reply)
          reply
        }
        case status => {
          println(s"$status error:${response.toString}")
          Future.failed(new IOException(s"Token request failed with status ${response.status} and error ${response.entity}"))
        }

      }

    }

  }

}