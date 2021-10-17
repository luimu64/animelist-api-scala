package com.anilist.controllers

import com.anilist.models._

object AnimeController {

  def getUserAnimelist(userid: String): String = {
    AnimeModel.getAnimeList(userid.toInt)
  }
}

