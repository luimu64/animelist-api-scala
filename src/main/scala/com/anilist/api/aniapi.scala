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

  get("/aniapi/list/get/:userid") {
    AnimeController.getUserAnimelist(params("userid"))
  }

  get("/aniapi/list/get/auth/:userid") {
    println(helpers.isLoggedIn(token.get))
    if (token.isDefined && helpers.isLoggedIn(token.get)) AnimeController.getUserAnimelist(params("userid"))
    else helpers.JsonError("not-authenticated")
  }

  post("/aniapi/login") {
    UserController.login(request.body)
  }

  post("/aniapi/register") {
    UserController.registerUser(request.body)
  }
}
