package com.anilist.api

import org.scalatra._
import com.anilist.models._

class aniapi extends ScalatraServlet {

  get("/v1/aniapi") {
    FetchAnimeData.getData
  }
}
