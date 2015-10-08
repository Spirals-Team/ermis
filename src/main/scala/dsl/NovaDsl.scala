package dsl

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl._
import com.woorea.openstack.nova.Nova
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


object NovaDsl {

  var novaClient: Nova=_

  def Request (host:String, port:Int) ={

    implicit val system = ActorSystem()
    implicit val materializer = ActorFlowMaterializer()



    val connectionFlow: Flow[HttpRequest, HttpResponse, Future[Http.OutgoingConnection]] =
    //   Http().outgoingConnection("http://akka.io")
      Http().outgoingConnection(host="localhost", port = 7371)

    val responseFuture: Future[HttpResponse] =
      Source.single(HttpRequest(uri = "/nova-demo"))
        .via(connectionFlow)
        .runWith(Sink.head)


    responseFuture.onSuccess{
      case value => {
        print("SUCCESS")
        print(value.entity.toStrict(5 seconds).map(_.data.decodeString("UTF-8")))
        print(value.entity.toStrict(5 seconds).map{_.data}.map(_.utf8String))
      }
    }
    responseFuture.onFailure{
      case value => {
        print("FAIL")
        print(value.getMessage)
      }
    }

  }

}