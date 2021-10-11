package com.anilist.api

import org.scalatra.test.scalatest._

class aniapiTests extends ScalatraFunSuite {

  addServlet(classOf[aniapi], "/*")

  test("GET / on aniapi should return status 200") {
    get("/") {
      status should equal (200)
    }
  }

}
