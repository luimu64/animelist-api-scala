package com.anilist.controllers

import com.anilist.controllers.UserController.{algo, key}
import pdi.jwt.JwtJson
import play.api.libs.json.{JsObject, Json}

object helpers {
  def filterQ(string: String): String = string.replace("\"", "")

  def isLoggedIn(token: String = ""): Boolean = {
    if (token != "") JwtJson.isValid(token, key, Seq(algo))
    else false
  }

  def JsonError(message: String): String = Json.stringify(Json.obj("error" -> message))

  def JsonResponse(message: String): String = Json.stringify(Json.obj("message" -> message))

  def JsonErrorAsObj(message: String): JsObject = Json.obj("error" -> message)

  def removeHTMLTags(input: String): String = {
    input.replaceAll("""<\w+?>""", "")
      .replaceAll("""</\w+?>""", "")
  }
}
