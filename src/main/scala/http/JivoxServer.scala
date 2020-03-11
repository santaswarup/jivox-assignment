package http

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import com.jivox.actor.JivoxFakeServices
import com.jivox.actor.JivoxFakeServices.InitiateFloodingOfRequests

import scala.concurrent.Future
import scala.io.StdIn

object JivoxServer extends App {

  implicit val system = ActorSystem("JivoxHttpServerSystem")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  val responseBackToClient = """
                               |<html>
                               | <body>
                               |   Please enter the context
                               | </body>
                               |</html>
        """.stripMargin
  val jivoxRoute =
    pathPrefix("jivox"){
      path("fakeMultiService"){
        get{
          complete(HttpEntity(
            ContentTypes.`text/html(UTF-8)`,
            responseBackToClient
          ))
        }~
          post{
           val jivoxFakeServiceActor = system.actorOf(Props[JivoxFakeServices],"jivoxFakeService")

            jivoxFakeServiceActor ! InitiateFloodingOfRequests
            complete(Future(StatusCodes.OK))
          }
      }
    }





  val bindingFuture = Http().bindAndHandle(jivoxRoute, "localhost", 8080)
  // wait for a new line, then terminate the server
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())

}
