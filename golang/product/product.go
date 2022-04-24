package product

import (
	"github.com/labstack/echo/v4"
	"golang/category"
	"gorm.io/gorm"
	"net/http"
	"strconv"
)

var db *gorm.DB
var err error

type Product struct {
	gorm.Model
	Name       string
	Price      uint
	Category   category.Category
	CategoryID uint
}

type ProductDTO struct {
	ID         uint
	Name       string
	Price      uint
	CategoryID uint
}

func InitProductMigration(db2 *gorm.DB, err2 error) {
	db = db2
	err = err2
	db.AutoMigrate(&Product{})
}

func getProductDto(productDto *ProductDTO, product Product) {
	productDto.ID = product.ID
	productDto.Name = product.Name
	productDto.Price = product.Price
	productDto.CategoryID = product.CategoryID
}

func GetProduct(context echo.Context) error {
	id := context.Param("id")
	var product Product
	db.Find(&product, id)
	var strId = strconv.FormatUint(uint64(product.ID), 10)
	if (strId) != id {
		return context.JSON(http.StatusInternalServerError, "Product not available with id: "+id)
	}
	var productDto ProductDTO
	getProductDto(&productDto, product)
	return context.JSON(http.StatusOK, &productDto)
}

func GetProducts(context echo.Context) error {
	var products []Product
	var productsDTO []ProductDTO
	db.Find(&products)
	if len(products) == 0 {
		return context.JSON(http.StatusInternalServerError, "There are no products")
	}

	for i := 0; i < len(products); i++ {
		var productDto ProductDTO
		getProductDto(&productDto, products[i])
		productsDTO = append(productsDTO, productDto)
	}

	return context.JSON(http.StatusOK, &productsDTO)
}

func SaveProduct(context echo.Context) error {
	product := new(Product)
	if err := (&echo.DefaultBinder{}).BindBody(context, &product); err != nil {
		return err
	}
	db.Create(&product)
	var strId = strconv.FormatUint(uint64(product.ID), 10)
	return context.JSON(http.StatusOK, "product added with id: "+strId)
}

func DeleteProduct(context echo.Context) error {
	id := context.Param("id")
	var product Product
	db.First(&product, id)
	var strId = strconv.FormatUint(uint64(product.ID), 10)
	if (strId) != id {
		return context.JSON(http.StatusInternalServerError, "Product not available")
	}
	db.Delete(&product)
	return context.JSON(http.StatusOK, "Product is deleted!!!")
}

func UpdateProduct(context echo.Context) error {
	id := context.Param("id")
	product := new(Product)
	db.First(&product, id)
	var strId = strconv.FormatUint(uint64(product.ID), 10)
	if (strId) != id {
		return context.JSON(http.StatusInternalServerError, "Product not available")
	}

	if err := (&echo.DefaultBinder{}).BindBody(context, &product); err != nil {
		return err
	}
	db.Save(&product)
	return context.JSON(http.StatusOK, &product)
}
