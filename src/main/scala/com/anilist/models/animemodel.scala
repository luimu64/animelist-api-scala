package com.anilist.models

import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet, SQLException}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import scala.collection.mutable.Buffer

case class AnimeTitle(
                       id: Int,
                       mal_id: Int,
                       name: String,
                       thumbnail: String,
                       status: String,
                       rating: String,
                       reasoning: String,
                       airing: Boolean,
                       episodes: Int,
                       score: Float,
                       synopsis: String,
                       url: String
                     )

object AnimeModel {
  var con: Connection = _

  def formatAnimeDataToJson(rs: ResultSet): String = {
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

    var animeData: Buffer[JsValue] = Buffer()
    while (rs.next()) {
      animeData += Json.toJson(
        AnimeTitle(
          rs.getInt("id"),
          rs.getInt("mal_id"),
          rs.getString("name"),
          rs.getString("thumbnail"),
          rs.getString("status"),
          rs.getString("rating"),
          rs.getString("reasoning"),
          rs.getBoolean("airing"),
          rs.getInt("episodes"),
          rs.getFloat("score"),
          rs.getString("synopsis"),
          rs.getString("url")
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
        e.printStackTrace()
        ""
    }
  }

  def addNewTitle(data: JsValue): Boolean = {
    val query: String = "INSERT INTO list (mal_id, name, thumbnail, status, rating, reasoning, airing, episodes, score, synopsis, url, userID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"

    var success: Int = 0

    try {
      con = DriverManager.getConnection(DbInfo.url, DbInfo.username, DbInfo.password)
      val stmt: PreparedStatement = con.prepareStatement(query)
      stmt.setInt(1, (data \ "data" \ "mal_id").as[Int])
      stmt.setString(2, (data \ "data" \ "title").as[String])
      stmt.setString(3, (data \ "data" \ "image_url").as[String])
      stmt.setBoolean(7, (data \ "data" \ "airing").as[Boolean])
      stmt.setInt(8, (data \ "data" \ "episodes").as[Int])
      stmt.setFloat(9, (data \ "data" \ "score").as[Float])
      stmt.setString(10, (data \ "data" \ "synopsis").as[String])
      stmt.setString(11, (data \ "data" \ "url").as[String])


      stmt.setString(4, (data \ "status").as[String])
      stmt.setString(5, (data \ "rating").as[String])
      stmt.setString(6, (data \ "reasoning").as[String])
      stmt.setInt(12, (data \ "userID").as[Int])

      success = stmt.executeUpdate()

      con.close()
      success == 1
    } catch {
      case e: SQLException =>
        e.printStackTrace()
        success == 1
    }
  }

  def deleteTitle(data: JsValue): Boolean = {
    val query: String = "DELETE FROM list WHERE (userID = ? and mal_id = ?) "

    var success: Int = 0

    try {
      con = DriverManager.getConnection(DbInfo.url, DbInfo.username, DbInfo.password)
      val stmt: PreparedStatement = con.prepareStatement(query)
      stmt.setInt(1, (data \ "userID").as[Int])
      stmt.setInt(2, (data \ "mal_id").as[Int])
      success = stmt.executeUpdate()

      con.close()
      success == 1
    } catch {
      case e: SQLException =>
        e.printStackTrace()
        success == 1
    }
  }

  def editTitle(data: JsValue): Boolean = {
    val query: String = "UPDATE list SET status = ?, rating = ?, reasoning = ? WHERE (userID = ? and mal_id = ?)"

    var success: Int = 0

    try {
      con = DriverManager.getConnection(DbInfo.url, DbInfo.username, DbInfo.password)
      val stmt: PreparedStatement = con.prepareStatement(query)
      stmt.setString(1, (data \ "status").as[String])
      stmt.setString(2, (data \ "rating").as[String])
      stmt.setString(3, (data \ "reasoning").as[String])
      stmt.setInt(4, (data \ "userID").as[Int])
      stmt.setInt(5, (data \ "mal_id").as[Int])
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
