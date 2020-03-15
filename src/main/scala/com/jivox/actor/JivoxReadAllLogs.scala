package com.jivox.actor

import akka.actor.{ActorLogging, ActorSystem}
import akka.persistence.PersistentActor
import akka.persistence.cassandra.query.scaladsl.CassandraReadJournal
import akka.persistence.query.PersistenceQuery
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

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
      implicit val dispatcher = context.dispatcher
      val logs:StringBuffer = new StringBuffer
      logs.append("Dummy")

     val ids = Future( persistenceIds.runForeach { id =>
        logs.append(s"\n $id")
        log.info(s"JivoxReadAllLogs: persistenceIds::::::::::::::::$id")
      })
      Await.ready(ids, Duration.Inf)
      sender() ! logs.toString
   }
  override def persistenceId: String = "JivoxLogHandle"
}
