package com.anilist.api

import org.scalatra._
import animodel._

class aniapi extends ScalatraServlet {

  get("/") {
    FetchAnimeData.getData()
  }

}
