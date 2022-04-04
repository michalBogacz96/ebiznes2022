package repository

import model.User
import scala.collection.mutable.ArrayBuffer
import javax.inject.{ Inject, Singleton }
import play.api._
import play.api.mvc._



@Singleton
class UserRepository @Inject()()() {


var users = ArrayBuffer[User]()

def deleteTheUser(id: Long): String = {
	var userArray = getUserById(id)
	if(userArray.isEmpty){
		"There is no element with given id."
	}else {
		var user = userArray(0)
		users -= user
		var userId = id
		"User with id : " +userId.toString + " was deleted "
	}
}

def getAllUsers(): ArrayBuffer[User] = {
	users
}


def getTheUser(id: Long): User = {
	getUserById(id)(0)
}

def updateTheUser(id: Long, firstName: String, lastName: String): String = {
	var userArray = getUserById(id)
	if(userArray.isEmpty){
		"There is no element with given id."
	}else {
		var user = userArray(0)
		user.firstName = firstName
		user.lastName = lastName
		var userId = user.id
		userId.toString
	}
}


def addTheUser(firstName: String, lastName: String): String = {
	var id: Long = -1
	if(users.isEmpty){
		id = 1
	}else {
		id = getLastIdFromList() + 1

	}
	var user = new User(id, firstName, lastName)
	users = users :+ user

	"User with id : " +id + " was added "
}


def getLastIdFromList(): Long = {
	users.last.id
}

def getUserById(id: Long): ArrayBuffer[User] ={
	users.filter(cat => cat.id == id)
}

}