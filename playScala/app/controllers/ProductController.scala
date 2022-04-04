package controllers

import repository.ProductRepository
import play.api.libs.json.Json
import model.User
import javax.inject._
import play.api._
import play.api.mvc._



@Singleton
class ProductController @Inject()(val productRepository: ProductRepository, val controllerComponents: ControllerComponents) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def getAllProducts() = Action { implicit request: Request[AnyContent] =>
    Ok(Json.toJson(productRepository.getAllProducts()))
  }


  def deleteTheProduct(id: Long) =  Action {implicit request: Request[AnyContent] =>
  	Ok(productRepository.deleteTheProduct(id))
  }


  def getTheProduct(id: Long) = Action {implicit request: Request[AnyContent] =>
    Ok(Json.toJson(productRepository.getTheProduct(id)))
  }

  def updateTheProduct(id: Long) = Action {implicit request: Request[AnyContent] =>
    var name = request.body.asJson.get("name").as[String]
    var category = request.body.asJson.get("category").as[String]
    var price = request.body.asJson.get("price").as[Int]

    Ok(productRepository.updateTheProduct(id, name, category, price))
  }


  def addTheProduct() =  Action {implicit request: Request[AnyContent]  =>
    var name = request.body.asJson.get("name").as[String]
    var category = request.body.asJson.get("category").as[String]
    var price = request.body.asJson.get("price").as[Int]
    Ok(productRepository.addTheProduct(name, category, price))
  }
}



