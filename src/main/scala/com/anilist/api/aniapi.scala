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

  post("/aniapi/login") {
    UserController.loginUser(request.body)
  }

  post("/aniapi/register") {
    UserController.registerUser(request.body)
  }

  post("/aniapi/list/add") {
    if (token.isDefined) {
      if (helpers.isLoggedIn(token.get)) AnimeController.addAnimeTitle(request.body)
      else helpers.JsonError("Not authenticated")
    } else helpers.JsonError("Not authenticated")
  }

  post("/aniapi/list/remove") {
    if (token.isDefined) {
      if (helpers.isLoggedIn(token.get)) AnimeController.deleteAnimeTitle(request.body)
      else helpers.JsonError("Not authenticated")
    } else helpers.JsonError("Not authenticated")
  }

  post("/aniapi/list/edit") {
    if (token.isDefined) {
      if (helpers.isLoggedIn(token.get)) AnimeController.editAnimeTitle(request.body)
      else helpers.JsonError("Not authenticated")
    } else helpers.JsonError("Not authenticated")
  }
}
