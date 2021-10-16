package com.anilist.models

import java.sql.{Connection, DriverManager, PreparedStatement}
import play.api.libs.json._
import play.api.libs.functional.syntax._

import scala.collection.mutable.Buffer

case class AnimeTitle(
                       id: Int,
                       name: String,
                       thumbnail: String,
                       status: String,
                       rating: String,
                       reasoning: String
                     )

object AnimeData {
  var con: Connection = _

  def getAnimeList(userid: Int): String = {
    val query: String = "SELECT * FROM list WHERE userID = ?"
    val url = "jdbc:mysql://localhost:3306/animelist"
    val username = "root"
    val password = ""

    try {
      con = DriverManager.getConnection(url, username, password)
      val stmt: PreparedStatement = con.prepareStatement(query)
      stmt.setInt(1, userid)
      val rs = stmt.executeQuery()


      //definitions for json library
      implicit val animeWrites: Writes[AnimeTitle] = (
        (JsPath \ "id").write[Int] and
          (JsPath \ "name").write[String] and
          (JsPath \ "thumbnail").write[String] and
          (JsPath \ "status").write[String] and
          (JsPath \ "rating").write[String] and
          (JsPath \ "reasoning").write[String]
        ) (unlift(AnimeTitle.unapply))

      var animeData: Buffer[JsValue] = Buffer()
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
      con.close()
      Json.stringify(Json.toJson(animeData))
    } catch {
      case e: Exception =>
        e.getMessage
    }
  }
}
