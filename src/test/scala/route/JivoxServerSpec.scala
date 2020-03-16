package route


import akka.http.scaladsl.model.StatusCodes

import akka.http.scaladsl.testkit.ScalatestRouteTest

import org.scalatest.{Matchers, WordSpec}



class JivoxServerspec extends WordSpec with Matchers with ScalatestRouteTest {
import http.JivoxServer.jivoxRoute
  "Jivox log Server" should {
    "push all failure logs into DB" in {
      Post("/jivox/pushFailureLogs") ~> jivoxRoute ~> check {
        //status shouldBe StatusCodes.OK //TODO

      }
    }

  }

}
