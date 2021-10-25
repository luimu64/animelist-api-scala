package com.anilist.controllers

import com.anilist.models._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import scala.collection.mutable.Buffer

object AnimeController {

  def formatAnimeDataToJson(animeData: Buffer[AnimeTitle]): String = {
    //template for json structure
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

    Json.stringify(Json.toJson(animeData))
  }

  def getUserAnimelist(userid: String): String = {
    formatAnimeDataToJson(AnimeModel.getAnimeList(userid.toInt))
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

