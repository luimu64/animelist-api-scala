package com.anilist.controllers

import pdi.jwt.{JwtJson, JwtAlgorithm}
import play.api.libs.json._
import com.anilist.models.usermodel

object UserController {
  val key = "placeholder"
  val algo = JwtAlgorithm.HS256

  def loginUser(reqBody: String): String = {
    val json = Json.parse(reqBody)
    val credentials = Json.obj("username" -> (json \ "username").get, "password" -> (json \ "password").get)
    val token = JwtJson.encode(credentials, key, algo)

    val userFromDb = UserModel.getUserByUsername(credentials("username").toString)

    if (userFromDb("password") == credentials("password")) {
      Json.stringify(Json.obj("token" -> token, "userID" -> userFromDb("userID")))
    } else helpers.JsonError("wrong-info")
  }

  def registerUser(reqBody: String): String = {
    val json = Json.parse(reqBody)
    val credentials = Credentials((json \ "username").as[String], (json \ "password").as[String])

    val success = UserModel.addUser(credentials)
    if (success) helpers.JsonResponse("registering-success")
    else helpers.JsonError("registering-user-failed")
  }
}