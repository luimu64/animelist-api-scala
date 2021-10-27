package com.anilist.controllers

import pdi.jwt.{JwtAlgorithm, JwtJson}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import com.anilist.models._
import com.github.t3hnar.bcrypt._

case class Credentials(username: String, password: String)

object UserController {
  val key = "placeholder"
  val algo = JwtAlgorithm.HS256

  implicit val UserWrites: Writes[User] = (
    (JsPath \ "username").write[String] and
      (JsPath \ "password").write[String] and
      (JsPath \ "userID").write[Int]
    ) (unlift(User.unapply))

  def formatUserToJson(user: User): String = Json.stringify(Json.toJson(user))

  def loginUser(reqBody: String): String = {
    val json = Json.parse(reqBody)
    val credentials = Json.obj("username" -> (json \ "username").get, "password" -> (json \ "password").get)
    val token = JwtJson.encode(credentials, key, algo)

    val userFromDb = UserModel.getUserByUsername(credentials("username").as[String])

    val pwFromRequest = credentials("password").as[String]
    if (userFromDb.username != "") {
      if (pwFromRequest.isBcryptedBounded(userFromDb.password)) {
        Json.stringify(Json.obj("token" -> token, "userID" -> userFromDb.userID))
      } else helpers.JsonError("Incorrect username or password")
    } else helpers.JsonError("User not found")
  }

  def registerUser(reqBody: String): String = {
    val json = Json.parse(reqBody)

    val hashedPw = (json \ "password").as[String].bcryptSafeBounded
    var credentials = Credentials((json \ "username").as[String], hashedPw.get)


    val success = UserModel.addUser(credentials)
    if (success) helpers.JsonResponse("registering-success")
    else helpers.JsonError("registering-user-failed")
  }
}