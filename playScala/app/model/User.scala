package model

import play.api.libs.json._

case class User(val id: Long,var firstName: String,var lastName: String)

object User {
  implicit val categoryFormat = Json.format[User]
}
