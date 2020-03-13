package com.jivox.actor

import java.util.UUID

import akka.actor.{ActorLogging, ActorSystem, Props}
import akka.persistence.PersistentActor
import akka.serialization.Serializer
import com.typesafe.config.ConfigFactory

case class JivoxServiceLogsFailure(tenant : String, hostedService: String, productName : String,location : String)


object JivoxLogHandler extends App {

  case class JivoxServiceDomainData(requestID: UUID, productName: String, location: String)

  val logHandlerActorSystem = ActorSystem("JivoxLogHandlerActorSystem",ConfigFactory.load("jivox.conf")
    .getConfig("jivoxFailureHandlerConfig"))
  val logHandler = logHandlerActorSystem.actorOf(Props[JivoxLogHandler],"jivoxLogHandler")

  val serviceFailurelogHandler = logHandlerActorSystem.actorOf(Props[JivoxReadAllLogs],"jivoxReadAllLogs")
}


class JivoxLogHandler extends PersistentActor with ActorLogging{



  override def receiveRecover: Receive = {
//    case JivoxServiceLogsSuccess =>
//      log.info(s"Recover from JivoxServiceSuccess::::::::::::::::")
    case serviceFailed@JivoxServiceLogsFailure(tenant, hostedService, productName, location) =>
      log.info(s"Recover from JivoxServiceFailure::::::::::::::::")


  }

  override def receiveCommand: Receive = {
//    case JivoxServiceLogsSuccess =>
//      log.info(s"Got service success::::::::::::::::")
    case serviceFailed@JivoxServiceLogsFailure(tenant, hostedService, productName, location) =>
      log.info(s"Got Srvice failure for ::::::::::::$tenant, $hostedService, $productName, $location")
      persist(serviceFailed) { e =>
        log.info(s"serviceFailed Persisted::::::::::::::::")
      }
    case _ => log.info(s"==========================")

  }

  override def persistenceId: String = "JivoxLogHandle"
}

class JivoxSerealizer extends Serializer{
  override def identifier: Int = 555333

  override def toBinary(o: AnyRef): Array[Byte] = o match {
    case event @ JivoxServiceLogsFailure(tenant, hostedService, productName, location) =>
      s"$tenant:$hostedService:$productName:$location".getBytes()
    case _ => throw new IllegalArgumentException("Other messages are not supplied")
  }
  override def includeManifest: Boolean = false

  override def fromBinary(bytes: Array[Byte], manifest: Option[Class[_]]): AnyRef = {
    val message = new String(bytes)
    val splitMessages = message.split(":")
    JivoxServiceLogsFailure(splitMessages(0),splitMessages(1),splitMessages(2),splitMessages(3))
  }
}
