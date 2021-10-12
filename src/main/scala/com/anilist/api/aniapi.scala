package com.anilist.api

import org.scalatra._
import com.anilist.models._
import org.scalatra.CorsSupport

class aniapi extends ScalatraServlet with CorsSupport {

  options("/*"){
    response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"))
  }

  get("/v1/aniapi") {
    FetchAnimeData.getData
  }
}
