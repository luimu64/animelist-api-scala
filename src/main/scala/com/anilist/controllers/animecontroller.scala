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

  def deleteAnimeTitle(reqBody: String): String = {
    if (AnimeModel.deleteTitle(Json.parse(reqBody))) helpers.JsonResponse("deleting-success")
    else helpers.JsonError("deleting-failed")
  }

  def editAnimeTitle(reqBody: String): String = {
    if (AnimeModel.editTitle(Json.parse(reqBody))) helpers.JsonResponse("editing-success")
    else helpers.JsonError("editing-failed")
  }
}

