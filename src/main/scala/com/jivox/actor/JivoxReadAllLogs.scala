package com.jivox.actor

import akka.actor.{ActorLogging, ActorSystem}
import akka.persistence.PersistentActor
import akka.persistence.cassandra.query.scaladsl.CassandraReadJournal
import akka.persistence.query.PersistenceQuery
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

object JivoxReadAllLogs{

  case object ReturnAllJivoxServiceLogsFailure
  val readJournalActorSystem = ActorSystem("readServiceLogJournal",ConfigFactory.load("jivox.conf")
    .getConfig("jivoxReadJournalConfig"))
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
    case _ =>
      log.info(s"JivoxReadAllLogs: Returning ReturnAllJivoxServiceLogsFailure::::::::::::::::")

      val readServiceLogJournal = PersistenceQuery(readJournalActorSystem).readJournalFor[CassandraReadJournal](CassandraReadJournal.Identifier)
      val persistenceIds = readServiceLogJournal.persistenceIds()
      implicit val materializer = ActorMaterializer()(readJournalActorSystem)
      persistenceIds.runForeach { id =>
        log.info(s"JivoxReadAllLogs: persistenceIds::::::::::::::::$id")
      }
    }
  override def persistenceId: String = "JivoxLogHandle"
}
