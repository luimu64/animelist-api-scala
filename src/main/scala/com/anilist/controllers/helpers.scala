package com.anilist.controllers

import com.anilist.controllers.UserController.{algo, key}
import pdi.jwt.JwtJson
import play.api.libs.json.Json

object helpers {
  def isLoggedIn(token: String): Boolean = JwtJson.isValid(token, key, Seq(algo))

  def JsonError(message: String): String = Json.stringify(Json.obj("error" -> message))
}
