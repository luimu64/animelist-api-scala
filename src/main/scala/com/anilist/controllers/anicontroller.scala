package com.anilist.controllers

import com.anilist.models._

object anicontroller {

  def getUserAnimelist(userid: String): String = {
    AnimeData.getAnimeList(userid)
  }

}
