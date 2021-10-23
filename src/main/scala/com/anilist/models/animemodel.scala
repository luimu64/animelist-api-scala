package com.anilist.models

import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet, SQLException}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import scala.collection.mutable.Buffer

case class AnimeTitle(
                       id: Int,
                       name: String,
                       thumbnail: String,
                       status: String,
                       rating: String,
                       reasoning: String
                     )

object AnimeModel {
  var con: Connection = _

  def formatAnimeDataToJson(rs: ResultSet): String = {
    //template for json structure
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
    Json.stringify(Json.toJson(animeData))
  }

  def getAnimeList(userid: Int): String = {
    val query: String = "SELECT * FROM list WHERE userID = ?"

    try {
      con = DriverManager.getConnection(DbInfo.url, DbInfo.username, DbInfo.password)
      val stmt: PreparedStatement = con.prepareStatement(query)
      stmt.setInt(1, userid)

      val rs = stmt.executeQuery()
      val animeDataJson = formatAnimeDataToJson(rs)

      con.close()
      animeDataJson
    } catch {
      case e: SQLException =>
        //returning stacktrace potential security risk
        e.printStackTrace().toString
    }
  }

  def addNewTitle(data: JsValue): Boolean = {
    val query: String = "INSERT INTO list (name, thumbnail, status, rating, reasoning, airing, episodes, score, synopsis, url, userID) VALUES (?,?,?,?,?,?,?,?,?,?,?)"

    var success: Int = 0

    try {
      con = DriverManager.getConnection(DbInfo.url, DbInfo.username, DbInfo.password)
      val stmt: PreparedStatement = con.prepareStatement(query)
      stmt.setString(1, (data \ "data" \ "title").as[String])
      stmt.setString(2, (data \ "data" \ "image_url").as[String])
      stmt.setBoolean(6, (data \ "data" \ "airing").as[Boolean])
      stmt.setInt(7, (data \ "data" \ "episodes").as[Int])
      stmt.setFloat(8, (data \ "data" \ "score").as[Float])
      stmt.setString(9, (data \ "data" \ "synopsis").as[String])
      stmt.setString(10, (data \ "data" \ "url").as[String])
      stmt.setInt(11, (data \ "userID").as[Int])

      stmt.setString(3, (data \ "status").as[String])
      stmt.setString(4, (data \ "rating").as[String])
      stmt.setString(5, (data \ "reasoning").as[String])

      success = stmt.executeUpdate()

      con.close()
      success == 1
    } catch {
      case e: SQLException =>
        e.printStackTrace()
        success == 1
    }
  }
}
