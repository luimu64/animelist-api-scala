package com.anilist.models

import java.sql.{Connection, DriverManager, PreparedStatement, SQLException}
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

  def existsInDb(titleData: JsValue): Boolean = {
    val query: String = "SELECT * FROM list WHERE (userID = ? and mal_id = ?)"
    var hasRows: Boolean = false

    try {
      con = DriverManager.getConnection(DbInfo.url, DbInfo.username, DbInfo.password)
      val stmt: PreparedStatement = con.prepareStatement(query)
      stmt.setInt(1, (titleData \ "userID").as[Int])
      stmt.setInt(2, (titleData \ "data" \ "mal_id").as[Int])

      val rs = stmt.executeQuery()

      hasRows = rs.next()
      con.close()
      hasRows
    } catch {
      case e: SQLException =>
        e.printStackTrace()
        hasRows
    }
  }

  def getAnimeList(userid: Int): Buffer[AnimeTitle] = {
    val query: String = "SELECT * FROM list WHERE userID = ?"
    var animeData: Buffer[AnimeTitle] = Buffer()

    try {
      con = DriverManager.getConnection(DbInfo.url, DbInfo.username, DbInfo.password)
      val stmt: PreparedStatement = con.prepareStatement(query)
      stmt.setInt(1, userid)

      val rs = stmt.executeQuery()

      while (rs.next()) {
        animeData +=
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
      }

      con.close()
      animeData
    } catch {
      case e: SQLException =>
        e.printStackTrace()
        animeData
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
