package com.jivox.actor

import java.util.UUID

import akka.actor.{Actor, ActorLogging, ActorSystem}

import com.typesafe.config.ConfigFactory

import scala.util.Random

object JivoxFakeServices{

  val jivoxFakeServiceActorSystem = ActorSystem("JivoxFakeServicesActorSystem",ConfigFactory.load("jivox.conf"))
  case object InitiateFloodingOfRequests

}

class JivoxFakeServices extends Actor with ActorLogging{
  import com.jivox.actor.JivoxFakeServices._
  import com.jivox.actor.JivoxLogHandler._
  import com.jivox.actor.JivoxReadAllLogs._

  override def receive: Receive = {
    case InitiateFloodingOfRequests =>
      log.info(s"Flooding of request from multiple services initiated")

      val logHandler = jivoxFakeServiceActorSystem.actorSelection("akka://JivoxLogHandlerActorSystem@localhost:5555/user/jivoxLogHandler")

      (0 to 10).foreach { _ =>
        val randomNumber = Random.nextInt()
        val uuid:UUID = UUID.randomUUID()
        val domainData: JivoxServiceDomainData = JivoxServiceDomainData(uuid,getProductName(),getLocation())
        logHandler ! JivoxServiceLogsFailure(getTenant(),getServer(), getProductName(), getLocation)

      }
    case ReturnAllJivoxServiceLogsFailure =>


      val failureServiceLog = jivoxFakeServiceActorSystem.actorSelection("akka://JivoxLogHandlerActorSystem@localhost:5555/user/jivoxReadAllLogs")
      failureServiceLog ! ReturnAllJivoxServiceLogsFailure
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
