package com.anilist.controllers

import com.anilist.models._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import scala.collection.mutable.Buffer

object AnimeController {

  implicit val animeWrites: Writes[AnimeTitle] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "mal_id").write[Int] and
      (JsPath \ "name").write[String] and
      (JsPath \ "thumbnail").write[String] and
      (JsPath \ "status").write[String] and
      (JsPath \ "rating").write[String] and
      (JsPath \ "reasoning").write[String] and
      (JsPath \ "airing").write[Boolean] and
      (JsPath \ "episodes").write[Int] and
      (JsPath \ "score").write[Float] and
      (JsPath \ "synopsis").write[String] and
      (JsPath \ "url").write[String]
    ) (unlift(AnimeTitle.unapply))

  def formatAnimeDataToJson(animeData: Buffer[AnimeTitle]): String = Json.stringify(Json.toJson(animeData))

  def getUserAnimelist(userid: String): String = {
    formatAnimeDataToJson(AnimeModel.getAnimeList(userid.toInt))
  }

  def addAnimeTitle(reqBody: String): String = {
    if (!AnimeModel.existsInDb(Json.parse(reqBody))) {
      if (AnimeModel.addNewTitle(Json.parse(reqBody))) helpers.JsonResponse("New series added")
      else helpers.JsonError("Adding failed")
    } else helpers.JsonError("Already on list")
  }

  def deleteAnimeTitle(reqBody: String): String = {
    if (AnimeModel.deleteTitle(Json.parse(reqBody))) helpers.JsonResponse("Deletion succeeded")
    else helpers.JsonError("Deletion failed")
  }

  def editAnimeTitle(reqBody: String): String = {
    if (AnimeModel.editTitle(Json.parse(reqBody))) helpers.JsonResponse("Editing succeeded")
    else helpers.JsonError("Editing failed")
  }
}

