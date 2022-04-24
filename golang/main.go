package main

import (
	"github.com/labstack/echo/v4"
	"golang/category"
	"golang/order"
	"golang/product"
	"golang/role"
	"golang/user"
	"gorm.io/driver/sqlite"
	"gorm.io/gorm"
)

func main() {

	db, err := gorm.Open(sqlite.Open("database.db"), &gorm.Config{})
	if err != nil {
		panic("failed to connect database")
	}

	user.InitUserMigration(db, err)
	role.InitRoleMigration(db, err)
	category.InitCategoryMigration(db, err)
	product.InitProductMigration(db, err)
	order.InitOrderMigration(db, err)

	admin := "admin"
	u := "user"
	var adminRole role.Role
	var userRole role.Role

	db.Find(&adminRole, role.Role{Name: admin})
	db.Find(&userRole, role.Role{Name: u})
	if adminRole.Name != admin {
		db.Create(&role.Role{Name: "admin"})
	}
	if userRole.Name != u {
		db.Create(&role.Role{Name: "user"})
	}

	echoServer := echo.New()
	UserRouters(echoServer)
	RoleRouters(echoServer)
	CategoryRouters(echoServer)
	ProductRouters(echoServer)
	OrderRouters(echoServer)
	echoServer.Logger.Fatal(echoServer.Start(":8000"))
}

func UserRouters(server *echo.Echo) {
	server.GET("/user/:id", user.GetUser)
	server.GET("/user", user.GetUsers)
	server.POST("/user", user.SaveUser)
	server.DELETE("/user/:id", user.DeleteUser)
	server.PUT("/user/:id", user.UpdateUser)
}

func RoleRouters(server *echo.Echo) {
	server.GET("/role/:id", role.GetRole)
	server.GET("/role", role.GetRoles)
	server.POST("/role", role.SaveRole)
	server.DELETE("/role/:id", role.DeleteRole)
	server.PUT("/role/:id", role.UpdateRole)
}

func CategoryRouters(server *echo.Echo) {
	server.GET("/category/:id", category.GetCategory)
	server.GET("/category", category.GetCategories)
	server.POST("/category", category.SaveCategory)
	server.DELETE("/category/:id", category.DeleteCategory)
	server.PUT("/category/:id", category.UpdateCategory)
}

func ProductRouters(server *echo.Echo) {
	server.GET("/product/:id", product.GetProduct)
	server.GET("/product", product.GetProducts)
	server.POST("/product", product.SaveProduct)
	server.DELETE("/product/:id", product.DeleteProduct)
	server.PUT("/product/:id", product.UpdateProduct)
}

func OrderRouters(server *echo.Echo) {
	server.GET("/order/:id", order.GetOrder)
	server.GET("/order", order.GetOrders)
	server.POST("/order", order.SaveOrder)
	server.DELETE("/order/:id", order.DeleteOrder)
	server.PUT("/order/:id", order.UpdateOrder)
}
