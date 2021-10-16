package com.anilist.api

import org.scalatra._
import com.anilist.controllers.anicontroller
import org.scalatra.CorsSupport

class aniapi extends ScalatraServlet with CorsSupport {

  options("/*") {
    response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"))
  }

  get("/aniapi/getAll/:userid") {
    anicontroller.getUserAnimelist(params("userid"))
  }
}
