package model

import play.api.libs.json._

case class Category(val id: Long,var name: String,var description: String)

object Category {
  implicit val categoryFormat = Json.format[Category]
}