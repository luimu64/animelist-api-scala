package com.anilist.controllers

import pdi.jwt.{JwtJson, JwtAlgorithm}
import play.api.libs.json._
import com.anilist.models.usermodel
import java.sql._

object UserController {
  val key = "placeholder"
  val algo = JwtAlgorithm.HS256

  def login(reqBody: String): String = {
    val json = Json.parse(reqBody)
    val claim = Json.obj("username" -> (json \ "username").get, "password" -> (json \ "password").get)
    val token = JwtJson.encode(claim, key, algo)

    val userFromDb = usermodel.getUserByUsername(claim("username").toString)

    if (userFromDb(1) == claim("password").toString()) Json.stringify(Json.obj("token" -> token, "userID" -> userFromDb(2)))
    else "Logging in failed"
  }

  def isLoggedIn(token: String): Boolean = {
    val decodedToken = JwtJson.decodeJson(token, key, Seq(algo))
    if (decodedToken.isSuccess) {
      val username = (decodedToken.get \ "username").as[String]
      val password = (decodedToken.get \ "password").as[String]
      val userFromDb = usermodel.getUserByUsername(username)
      if (password == userFromDb(1)) true
      else false
    } else false
  }
}
