package com.anilist.models

import java.sql.{Connection, DriverManager, PreparedStatement, SQLException}
import scala.collection.mutable.Buffer

object usermodel {
  var con: Connection = _

  def getUserByUsername(username: String): Buffer[String] = {
    val query: String = "SELECT * FROM users WHERE username = ?"

    try {
      con = DriverManager.getConnection(DbInfo.url, DbInfo.username, DbInfo.password)
      val stmt: PreparedStatement = con.prepareStatement(query)
      stmt.setString(1, username)

      val rs = stmt.executeQuery()
      var user: Buffer[String] = Buffer()

      if (rs.next()) {
        user = Buffer(
          rs.getString("username"),
          rs.getString("password"),
          rs.getString("userID"))
      } else user = Buffer()

      con.close()
      user
    } catch {
      case e: SQLException =>
        //returning stacktrace potential security risk
        e.printStackTrace()
        Buffer()
    }
  }
}
