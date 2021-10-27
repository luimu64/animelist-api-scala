package com.anilist.models

import play.api.libs.json._
import com.anilist.controllers.{helpers, Credentials}
import java.sql.{Connection, DriverManager, PreparedStatement, SQLException}

case class User(username: String, password: String, userID: Int)

object UserModel {
  var con: Connection = _

  def getUserByUsername(username: String): User = {
    val query: String = "SELECT * FROM users WHERE username = ?"
    var user: User = User("", "", 0)

    try {
      con = DriverManager.getConnection(DbInfo.url, DbInfo.username, DbInfo.password)
      val stmt: PreparedStatement = con.prepareStatement(query)
      stmt.setString(1, helpers.filterQ(username))

      val rs = stmt.executeQuery()

      if (rs.next()) {
        user = User(
          rs.getString("username"),
          rs.getString("password"),
          rs.getInt("userID"))
      }

      con.close()
      user
    } catch {
      case e: SQLException =>
        e.printStackTrace()
        con.close()
        user
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
