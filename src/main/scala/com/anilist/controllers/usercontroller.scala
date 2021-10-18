package com.anilist.controllers

import pdi.jwt.{JwtJson, JwtAlgorithm}
import play.api.libs.json._
import com.anilist.models.usermodel

object UserController {
  val key = "placeholder"
  val algo = JwtAlgorithm.HS256

  def login(reqBody: String): String = {
    val json = Json.parse(reqBody)
    val credentials = Json.obj("username" -> (json \ "username").get, "password" -> (json \ "password").get)
    val token = JwtJson.encode(credentials, key, algo)

    val userFromDb = usermodel.getUserByUsername(credentials("username").toString)

    if (userFromDb("password") == credentials("password")) {
      Json.stringify(Json.obj("token" -> token, "userID" -> userFromDb("userID")))
    } else helpers.JsonError("wrong-info")
  }
}