package com.anilist.api

import org.scalatra._
import com.anilist.controllers._
import org.scalatra.CorsSupport

class aniapi extends ScalatraServlet with CorsSupport {
  var token: Option[String] = _

  options("/*") {
    response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"))
  }

  before() {
    token = Option(request.getHeader("Authentication"))
  }

  get("/aniapi/getAll/:userid") {
    AnimeController.getUserAnimelist(params("userid"))
  }

  get("/aniapi/getMyList") {
    if (token.isDefined && UserController.isLoggedIn(token.get)) AnimeController.getUserAnimelist("1")
    else "You are not logged in"
  }

  post("/aniapi/login") {
    UserController.login(request.body)
  }
}
