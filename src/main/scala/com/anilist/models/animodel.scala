package animodel

import java.sql.{Connection, DriverManager}
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

object FetchAnimeData extends App {
  var connection: Connection = _

  def getData(): String = {
    val url = "jdbc:mysql://localhost:3306/animelist"
    val username = "root"
    val password = ""
    var json = ""

    try {
      connection = DriverManager.getConnection(url, username, password)
      val statement = connection.createStatement
      val rs = statement.executeQuery("SELECT * FROM list")
      while (rs.next()) {
        val name = ("name" -> rs.getString("name"))
        val thumbnail = ("thumbnail" -> rs.getString("thumbnail"))
        val status = ("status" -> rs.getString("status"))
        val rating = ("rating" -> rs.getString("rating"))
        val reasoning = ("reasoning" -> rs.getString("reasoning"))
        json =
          json + compact(render(name ~ thumbnail ~ status ~ rating ~ reasoning))
      }
      connection.close()
      "[" + json + "]"
    } catch {
      case e: Exception =>
        e.printStackTrace
        "Something went wrong"
    }
  }
}
