package repository

import model.Category
import scala.collection.mutable.ArrayBuffer
import javax.inject.{ Inject, Singleton }
import play.api._
import play.api.mvc._



@Singleton
class CategoryRepository @Inject()()() {

var categories = ArrayBuffer[Category]()


def deleteTheCategory(id: Long): String = {

	var categoryArray = getCategoryById(id)
	if(categoryArray.isEmpty){
		"There is no element with given id."
	}else {
		var category = categoryArray(0)
		categories -= category
		var categoryId = id
		"Category with id : " +categoryId.toString + " was deleted "
	}
}


def getAllCategories(): ArrayBuffer[Category] = {
	categories
}


def getTheCategory(id: Long): Category = {
	getCategoryById(id)(0)
}

def updateTheCategory(id: Long, name: String, description: String): String = {
	var categoryArray = getCategoryById(id)
	if(categoryArray.isEmpty){
		"There is no element with given id."
	}else {
		var category = categoryArray(0)
		category.name = name
		category.description = description
		var categoryId = id
		categoryId.toString
	}
}


def addTheCategory(name: String, description: String): String = {
	var id: Long = -1
	if(categories.isEmpty){
		id = 1
	}else {
		id = getLastIdFromList() + 1

	}
	var category = new Category(id, name, description)
	categories = categories :+ category
	"Category with id : " +id + " was added "
}


def getLastIdFromList(): Long = {
	categories.last.id
}

def getCategoryById(id: Long): ArrayBuffer[Category] = {
	categories.filter(cat => cat.id == id)
}


}