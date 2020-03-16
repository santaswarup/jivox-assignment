package com.jivox.actor

import akka.actor.{Actor, ActorLogging, ActorSystem}
import akka.persistence.PersistentActor
import akka.persistence.cassandra.query.scaladsl.CassandraReadJournal
import akka.persistence.query.PersistenceQuery
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

import scala.util._

object JivoxReadAllLogs{
  case object ReadAllIds
  val readJournalActorSystem = ActorSystem("readServiceLogJournal",ConfigFactory.load("jivox.conf")
    .getConfig("jivoxFailureHandlerConfig"))
}

class JivoxReadAllLogs() extends Actor with ActorLogging{
  import JivoxReadAllLogs._



  override def receive: Receive = {
    case _ =>
      log.info(s"JivoxReadAllLogs: Returning ReturnAllJivoxServiceLogsFailure::::::::::::::::")

      val readServiceLogJournal = PersistenceQuery(readJournalActorSystem).readJournalFor[CassandraReadJournal](CassandraReadJournal.Identifier)
      val persistenceIds = readServiceLogJournal.currentPersistenceIds()
      val persistenceevents = readServiceLogJournal.eventsByPersistenceId("JivoxLogHandle",0,20)
      implicit val materializer = ActorMaterializer()(readJournalActorSystem)
      implicit val dispatcher = context.dispatcher
      val logs:StringBuffer = new StringBuffer
      logs.append("Dummy")
      persistenceIds.runForeach { ids =>
        logs.append(ids)
        log.info(s"JivoxReadAllLogs: persistenceids::::::::::::::::$ids")
      }.onComplete {
        case Success(_) =>       log.info(s"JivoxReadAllLogs: persistenceIds onComplete:Success:::::::::::::::$logs")
          sender() ! logs.toString
        case Failure(_) => log.info(s"JivoxReadAllLogs: persistenceIds onComplete:Failed:::::::::::::::$logs")

      }
      persistenceevents.runForeach { events =>

        log.info(s"JivoxReadAllLogs: events::::::::::::::::$events")
      }.onComplete {
        case Success(_) =>       log.info(s"JivoxReadAllLogs:persistenceevents onComplete:Success:::::::::::::::")

        case Failure(_) => log.info(s"JivoxReadAllLogs:persistenceevents onComplete:Failed:::::::::::::::")

      }


   }
}
