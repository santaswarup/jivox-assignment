package http

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import com.jivox.actor.JivoxFakeServices
import com.jivox.actor.JivoxFakeServices.InitiateFloodingOfRequests
import com.jivox.actor.JivoxReadAllLogs.ReturnAllJivoxServiceLogsFailure
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.Future
import scala.util.Success
import scala.io.StdIn

object JivoxServer extends App {

  implicit val system = ActorSystem("JivoxHttpServerSystem")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  val jivoxFakeServiceActor = system.actorOf(Props[JivoxFakeServices],"jivoxFakeService")


  val jivoxRoute =
    pathPrefix("jivox"){
      path("fakeMultiService"){
        post{


            jivoxFakeServiceActor ! InitiateFloodingOfRequests
            complete(Future(StatusCodes.OK))
          }
      }~
      path("getAllFailureServiceLogs"){
        get{

          implicit val timeout:Timeout = Timeout(1 second)

          val failureLogs:Seq[String] = Seq()
          val allFailureLogs = jivoxFakeServiceActor ? ReturnAllJivoxServiceLogsFailure
          allFailureLogs.onComplete {
            case Success(logs) =>
              failureLogs ++ logs.asInstanceOf[Seq[String]]
            case _ =>

          }
          val responseBackToClient = s"""
                                       |<html>
                                       | <body>
                                       |   All failure logs below:
                                       |   $failureLogs
                                       | </body>
                                       |</html>
        """.stripMargin
          complete(HttpEntity(
            ContentTypes.`text/html(UTF-8)`,
            responseBackToClient
          ))
        }
      }
    }





  val bindingFuture = Http().bindAndHandle(jivoxRoute, "172.31.32.110", 80)
  // wait for a new line, then terminate the server
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())

}
