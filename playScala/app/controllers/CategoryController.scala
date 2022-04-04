package controllers

import repository.CategoryRepository
import play.api.libs.json.Json
import model.User
import javax.inject._
import play.api._
import play.api.mvc._



@Singleton
class CategoryController @Inject()(val categoryRepository: CategoryRepository, val controllerComponents: ControllerComponents) extends BaseController {


  def getAllCategories() = Action { implicit request: Request[AnyContent] =>
    Ok(Json.toJson(categoryRepository.getAllCategories()))
  }


  def deleteTheCategory(id: Long) =  Action {implicit request: Request[AnyContent] =>
    Ok(categoryRepository.deleteTheCategory(id))
  }

  def getTheCategory(id: Long) = Action {implicit request: Request[AnyContent] =>
    Ok(Json.toJson(categoryRepository.getTheCategory(id)))
  }

  def updateTheCategory(id: Long) = Action {implicit request: Request[AnyContent] =>
    var name = request.body.asJson.get("name").as[String]
    var description = request.body.asJson.get("description").as[String]
    Ok(categoryRepository.updateTheCategory(id, name, description))
  }

  def addTheCategory() =  Action {implicit request: Request[AnyContent]  =>
    var name = request.body.asJson.get("name").as[String]
    var description = request.body.asJson.get("description").as[String]
    Ok(categoryRepository.addTheCategory(name, description))
  }
}