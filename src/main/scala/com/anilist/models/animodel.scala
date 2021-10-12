package com.anilist.models

import java.sql.{Connection, DriverManager}
import play.api.libs.json._
import scala.collection.immutable._
import play.api.libs.functional.syntax._
import scala.collection.mutable.Buffer

object FetchAnimeData extends App {
  var connection: Connection = _

  def getData: String = {
    val url = "jdbc:mysql://localhost:3306/animelist"
    val username = "root"
    val password = ""

    case class AnimeTitle(
        id: Int,
        name: String,
        thumbnail: String,
        status: String,
        rating: String,
        reasoning: String
    )

    try {
      connection = DriverManager.getConnection(url, username, password)
      val statement = connection.createStatement
      val rs = statement.executeQuery("SELECT * FROM list")

      var animeData: Buffer[JsValue] = Buffer()

      implicit val animeWrites: Writes[AnimeTitle] = (
        (JsPath \ "id").write[Int] and
          (JsPath \ "name").write[String] and
          (JsPath \ "thumbnail").write[String] and
          (JsPath \ "status").write[String] and
          (JsPath \ "rating").write[String] and
          (JsPath \ "reasoning").write[String]
      )(unlift(AnimeTitle.unapply))

      while (rs.next()) {
        animeData += Json.toJson(
          AnimeTitle(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("thumbnail"),
            rs.getString("status"),
            rs.getString("rating"),
            rs.getString("reasoning")
          )
        )
      }
      connection.close()
      Json.stringify(Json.toJson(animeData))
    } catch {
      case e: Exception =>
        e.getMessage
    }
  }
}
