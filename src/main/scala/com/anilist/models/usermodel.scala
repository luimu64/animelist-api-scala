package com.anilist.models

import play.api.libs.json._
import com.anilist.controllers.{helpers, Credentials}
import java.sql.{Connection, DriverManager, PreparedStatement, SQLException}

object UserModel {
  var con: Connection = _

  def getUserByUsername(username: String): JsObject = {
    val query: String = "SELECT * FROM users WHERE username = ?"

    try {
      con = DriverManager.getConnection(DbInfo.url, DbInfo.username, DbInfo.password)
      val stmt: PreparedStatement = con.prepareStatement(query)
      stmt.setString(1, helpers.filterQ(username))

      val rs = stmt.executeQuery()
      var user: JsObject = Json.obj()

      if (rs.next()) {
        user = Json.obj(
          "username" -> rs.getString("username"),
          "password" -> rs.getString("password"),
          "userID" -> rs.getString("userID"))
      } else user = Json.obj()

      con.close()
      user
    } catch {
      case e: SQLException =>
        e.printStackTrace()
        con.close()
        helpers.JsonErrorAsObj("getting-user-from-database-failed")
    }
  }

  def addUser(cr: Credentials): Boolean = {
    val query: String = "INSERT INTO users (username, password) VALUES (?, ?)"
    //executeUpdate returns number of affected rows
    var success: Int = 0

    try {
      con = DriverManager.getConnection(DbInfo.url, DbInfo.username, DbInfo.password)
      val stmt: PreparedStatement = con.prepareStatement(query)
      stmt.setString(1, cr.username)
      stmt.setString(2, cr.password)

      success = stmt.executeUpdate()
      con.close()
      success == 1
    } catch {
      case e: SQLException =>
        e.printStackTrace()
        con.close()
        success == 1
    }
  }
}
