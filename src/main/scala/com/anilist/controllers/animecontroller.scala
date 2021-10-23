package com.anilist.controllers

import com.anilist.models._
import play.api.libs.json._

object AnimeController {

  def getUserAnimelist(userid: String): String = {
    AnimeModel.getAnimeList(userid.toInt)
  }

  def addAnimeTitle(reqBody: String): String = {
    if (AnimeModel.addNewTitle(Json.parse(reqBody))) helpers.JsonResponse("adding-success")
    else helpers.JsonError("adding-failed")
  }
}

