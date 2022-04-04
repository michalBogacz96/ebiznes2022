package repository

import scala.collection.mutable.ArrayBuffer
import model.Product
import javax.inject.{ Inject, Singleton }
import play.api._
import play.api.mvc._



@Singleton
class ProductRepository @Inject()()() {


var products = ArrayBuffer[Product]()


def getProductById(id: Long): ArrayBuffer[Product] ={
	products.filter(cat => cat.id == id)
}

def deleteTheProduct(id: Long): String = {
	var productArray = getProductById(id)
	if(productArray.isEmpty){
		"There is no element with given id."
	}else {
		var product = productArray(0)
		products -= product
		var productId = id
		"Product with id : " +productId.toString + " was deleted "
	}
}

def getAllProducts(): ArrayBuffer[Product] = {
	products
}


def getTheProduct(id: Long): Product = {
	getProductById(id)(0)
}

def updateTheProduct(id: Long, name: String, category: String, price: Int): String = {
	var productArray = getProductById(id)
	if(productArray.isEmpty){
		"There is no element with given id."
	}else {
		val productToBeUpdated = productArray(0)
		productToBeUpdated.name = name
		productToBeUpdated.category = category
		productToBeUpdated.price = price
		var productId = id
		productId.toString
	}
}


def addTheProduct(name: String, category: String, price: Int): String = {
	var id: Long = -1
	if(products.isEmpty){
		id = 1
	}else {
		id = getLastIdFromList() + 1

	}
	var product = new Product(id, name, category, price)
	products = products :+ product

	"Product with id : " +id + " was added "
}


def getLastIdFromList(): Long = {
	products.last.id
}

}