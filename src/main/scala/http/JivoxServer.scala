package http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import scala.io.StdIn

object JivoxServer extends App {

  implicit val system = ActorSystem("JivoxHttpServerSystem")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  val jivoxRoute =
    path("prob"){
      get{
        complete(StatusCodes.Ok)
      }~
      post{

      }~
        pathEndOrSingleSlash {
          complete(HttpEntity(
            ContentTypes.`text/html(UTF-8)`,
            """
              |<html>
              | <body>
              |   Please enter the context
              | </body>
              |</html>
        """.stripMargin
          ))
        }

    }




  val bindingFuture = Http().bindAndHandle(jivoxRoute, "localhost", 8080)
  // wait for a new line, then terminate the server
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())

}
