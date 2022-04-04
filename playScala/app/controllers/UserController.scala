package controllers

import repository.UserRepository
import play.api.libs.json.Json
import model.User
import javax.inject._
import play.api._
import play.api.mvc._



@Singleton
class UserController @Inject()(val userRepository: UserRepository, val controllerComponents: ControllerComponents) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def getAllUsers() = Action { implicit request: Request[AnyContent] =>
    Ok(Json.toJson(userRepository.getAllUsers()))
  }


  def deleteTheUser(id: Long) =  Action {implicit request: Request[AnyContent] =>
  	Ok(userRepository.deleteTheUser(id))
  }


  def getTheUser(id: Long) = Action {implicit request: Request[AnyContent] =>
    Ok(Json.toJson(userRepository.getTheUser(id)))
  }

  def updateTheUser(id: Long) = Action {implicit request: Request[AnyContent] =>
    var firstName = request.body.asJson.get("firstName").as[String]
    var lastName = request.body.asJson.get("lastName").as[String]
    Ok(userRepository.updateTheUser(id, firstName, lastName))
  }


  def addTheUser() =  Action {implicit request: Request[AnyContent]  =>
    var firstName = request.body.asJson.get("firstName").as[String]
    var lastName = request.body.asJson.get("lastName").as[String]
    Ok(userRepository.addTheUser(firstName, lastName))
  }


}



