package com.jivox.actor

import java.util.UUID

import akka.actor.{Actor, ActorLogging, ActorSystem}
import com.typesafe.config.ConfigFactory

import scala.util.Random

object JivoxFakeServices{

  case class JivoxServiceDomainData(requestID: UUID, productName: String, location: String)
  case class JivoxServiceFailure(tenant : String, hostedService: String, domainData : JivoxServiceDomainData )
  case object JivoxServiceSuccess
  case object InitiateFloodingOfRequests

}

class JivoxFakeServices extends Actor with ActorLogging{
  import com.jivox.actor.JivoxFakeServices._
  override def receive: Receive = {
    case InitiateFloodingOfRequests =>
      log.info(s"Flooding of request from multiple services initiated")
      val jivoxFakeServiceActorSystem = ActorSystem("JivoxFakeServicesActorSystem",ConfigFactory.load("jivox.conf"))
      val logHandler = jivoxFakeServiceActorSystem.actorSelection("akka://JivoxLogHandlerActorSystem@localhost:5555/user/jivoxLogHandler")

      (0 to 100).foreach { _ =>
        val randomNumber = Random.nextInt()
        val uuid:UUID = UUID.randomUUID()
        val domainData: JivoxServiceDomainData = JivoxServiceDomainData(uuid,getProductName(),getLocation())
        if(randomNumber % 2 == 0){
          log.info("Sending serviceSuccess to LogHandler")
          logHandler ! JivoxServiceSuccess
        }else
        {
          log.info("Sending serviceFailure to LogHandler")
          logHandler ! JivoxServiceFailure(getTenant(),getServer(), domainData)
        }
      }
  }

  def getProductName(): String =
  {
    val products = Seq("adwords-1", "chatbots", "adds", "page-click")
    products(Random.nextInt(products.length))
  }

  def getTenant(): String =
  {
    val tenant = Seq("Microsoft", "Google", "Amazon", "Netflix")
    tenant(Random.nextInt(tenant.length))
  }


  def getLocation(): String =
  {
    val location = Seq("USA", "INDIA", "JAPAN", "UK")
    location(Random.nextInt(location.length))
  }

  def getServer(): String =
  {
    val location = Seq("Apache webserver", "Java based server", "PHP based server", "database server", "Integration server")
    location(Random.nextInt(location.length))
  }
}
