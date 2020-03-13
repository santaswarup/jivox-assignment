package com.jivox.actor

import akka.actor.ActorLogging
import akka.persistence.PersistentActor

object JivoxReadAllLogs{

  case object ReturnAllJivoxServiceLogsFailure
}

class JivoxReadAllLogs() extends PersistentActor with ActorLogging{
  import JivoxReadAllLogs._
 // val serviceLogs: Map[UUID,JivoxServiceLogsFailure] = Map()
  override def receiveRecover: Receive = {
     case serviceFailed@JivoxServiceLogsFailure(tenant, hostedService, productName, location) =>

     // serviceLogs + (domainData.requestID -> serviceFailed)
      log.info(s"JivoxReadAllLogs: Recover from JivoxServiceFailure from persistent store::::::::::::::::$serviceFailed")
     case _ =>
  }



  override def receiveCommand: Receive = {
    case ReturnAllJivoxServiceLogsFailure =>
      log.info(s"JivoxReadAllLogs: Returning ReturnAllJivoxServiceLogsFailure::::::::::::::::")

  }

  override def persistenceId: String = "JivoxLogHandle"
}
