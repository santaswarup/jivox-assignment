### Log sequencing service

##Design

In order to handle this problem, A clustered Persistent actor named `JivoxLogHandler` is created. 
This actor receives messages in the form of JivoxServiceLogsFailure(tenant, hostedService, productName, location) 
and Store into Cassandra DB as an Event. By spinning up multiple nodes in akka cluster, 
actor can handle millions of Async requests and stores all failure logs.
    #Pros:
     - Sending messages to this cluster can be done via REST call or using actor selection. So that any module can use it as plug and play.
     - Communication is asynchronous.
     - By using cassandra Journal, we can store and process millions of failure logs.
     - Since the event stores domain specific data like tenant, hostedService, productName, location. 
     By tagging logs can be segregated and read based on tags.
     - In future this can be extended to recreate specific pattern of cause of failure by recreating events so that Fix can be applied on bulk.
     
 #Usage :
  -REST : 
        Populate Logs : http://localhost:80/jivox/pushFailureLogs   : Each call push 1000 failure into cassandra Journal by sending message to actor. 
        This is just a simulation of problem.
  -Actor Selection :  System.actorSelection("akka://JivoxLogHandlerActorSystem@localhost:5555/user/jivoxLogHandler") then send message to actor.
  
 Then API's can be built to get the logs, delete fixed issue from Journal etc.
 Sample API : To fetch all persistence IDs. This Ids can be customized later like for each tenant there will be specific IDs.
       http://localhost:80/jivox/getAllFailureServiceLogs
 
