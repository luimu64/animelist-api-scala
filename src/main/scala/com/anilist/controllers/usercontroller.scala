package com.anilist.controllers

import pdi.jwt.{JwtAlgorithm, JwtJson}
import play.api.libs.json._
import com.anilist.models.UserModel._
import com.github.t3hnar.bcrypt._

case class Credentials(username: String, password: String)

object UserController {
  val key = "placeholder"
  val algo = JwtAlgorithm.HS256

  def loginUser(reqBody: String): String = {
    val json = Json.parse(reqBody)
    val credentials = Json.obj("username" -> (json \ "username").get, "password" -> (json \ "password").get)
    val token = JwtJson.encode(credentials, key, algo)

    val userFromDb = getUserByUsername(credentials("username").as[String])

    val pwFromRequest = credentials("password").as[String]
    if (userFromDb != Json.obj()) {
      if (pwFromRequest.isBcryptedBounded(userFromDb("password").as[String])) {
        Json.stringify(Json.obj("token" -> token, "userID" -> userFromDb("userID")))
      } else helpers.JsonError("wrong-info")
    } else helpers.JsonError("user-not-in-database")
  }

  def registerUser(reqBody: String): String = {
    val json = Json.parse(reqBody)

    val hashedPw = (json \ "password").as[String].bcryptSafeBounded
    var credentials = Credentials((json \ "username").as[String], hashedPw.get)

    val success = addUser(credentials)
    if (success) helpers.JsonResponse("registering-success")
    else helpers.JsonError("registering-user-failed")
  }
}