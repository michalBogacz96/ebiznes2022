package model

import play.api.libs.json._

case class Product(val id: Long,var name: String,var category: String, var price: Int)

object Product {
  implicit val categoryFormat = Json.format[Product]
}