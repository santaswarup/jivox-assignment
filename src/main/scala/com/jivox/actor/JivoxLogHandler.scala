package com.jivox.actor

import akka.actor.{ActorLogging, ActorSystem, Props}
import akka.persistence.PersistentActor
import com.jivox.actor.JivoxFakeServices.{JivoxServiceFailure, JivoxServiceSuccess}
import com.typesafe.config.ConfigFactory

object JivoxLogHandler extends App {
  val logHandlerActorSystem = ActorSystem("JivoxLogHandlerActorSystem",ConfigFactory.load("jivox.conf")
    .getConfig("jivoxFailureHandlerConfig"))
  val logHandler = logHandlerActorSystem.actorOf(Props[JivoxLogHandler],"jivoxLogHandler")

}

class JivoxLogHandler extends PersistentActor with ActorLogging{


  override def receiveRecover: Receive = {
    case JivoxServiceSuccess =>
      log.info(s"Recover from JivoxServiceSuccess::::::::::::::::")
    case serviceFailed@JivoxServiceFailure(tenant, hostedService, domainData) =>
      log.info(s"Recover from JivoxServiceFailure::::::::::::::::")


  }

  override def receiveCommand: Receive = {
    case JivoxServiceSuccess =>
      log.info(s"Got service success::::::::::::::::")
    case serviceFailed@JivoxServiceFailure(tenant, hostedService, domainData) =>
      log.info(s"Got Srvice failure for ::::::::::::$tenant, $hostedService, $domainData")
      persist(serviceFailed) { e =>
        log.info(s"serviceFailed Persisted::::::::::::::::")
      }

  }

  override def persistenceId: String = "JivoxLogHandle"
}
