package order

import (
	"github.com/labstack/echo/v4"
	"golang/product"
	"gorm.io/gorm"
	"net/http"
	"strconv"
	"time"
)

var db *gorm.DB
var err error

type Order struct {
	gorm.Model
	Date      time.Time
	Product   product.Product
	ProductID uint
	isPaid    bool
}

type OrderDTO struct {
	ID        uint
	Date      time.Time
	ProductID uint
	isPaid    bool
}

func InitOrderMigration(db2 *gorm.DB, err2 error) {
	db = db2
	err = err2
	db.AutoMigrate(&Order{})
}

func getOrderDto(orderDto *OrderDTO, order Order) {
	orderDto.ID = order.ID
	orderDto.Date = order.Date
	orderDto.isPaid = order.isPaid
	orderDto.ProductID = order.ProductID
}

func GetOrder(context echo.Context) error {
	id := context.Param("id")
	var order Order
	db.Find(&order, id)
	var strId = strconv.FormatUint(uint64(order.ID), 10)
	if (strId) != id {
		return context.JSON(http.StatusInternalServerError, "Order not available with id: "+id)
	}
	var orderDto OrderDTO
	getOrderDto(&orderDto, order)
	return context.JSON(http.StatusOK, &orderDto)
}

func GetOrders(context echo.Context) error {
	var orders []Order
	var ordersDTO []OrderDTO
	db.Find(&orders)
	if len(orders) == 0 {
		return context.JSON(http.StatusInternalServerError, "There are no orders")
	}

	for i := 0; i < len(orders); i++ {
		var orderDto OrderDTO
		getOrderDto(&orderDto, orders[i])
		ordersDTO = append(ordersDTO, orderDto)
	}

	return context.JSON(http.StatusOK, &ordersDTO)
}

func SaveOrder(context echo.Context) error {
	order := new(Order)
	if err := (&echo.DefaultBinder{}).BindBody(context, &order); err != nil {
		return err
	}
	db.Create(&order)
	var strId = strconv.FormatUint(uint64(order.ID), 10)
	return context.JSON(http.StatusOK, "order added with id: "+strId)
}

func DeleteOrder(context echo.Context) error {
	id := context.Param("id")
	var order Order
	db.First(&order, id)
	var strId = strconv.FormatUint(uint64(order.ID), 10)
	if (strId) != id {
		return context.JSON(http.StatusInternalServerError, "Order not available")
	}
	db.Delete(&order)
	return context.JSON(http.StatusOK, "Order is deleted!!!")
}

func UpdateOrder(context echo.Context) error {
	id := context.Param("id")
	order := new(Order)
	db.First(&order, id)
	var strId = strconv.FormatUint(uint64(order.ID), 10)
	if (strId) != id {
		return context.JSON(http.StatusInternalServerError, "Order not available")
	}

	if err := (&echo.DefaultBinder{}).BindBody(context, &order); err != nil {
		return err
	}
	db.Save(&order)
	return context.JSON(http.StatusOK, &order)
}
