package category

import (
	"github.com/labstack/echo/v4"
	"gorm.io/gorm"
	"net/http"
	"strconv"
)

var db *gorm.DB
var err error

type Category struct {
	gorm.Model
	Name string
}

type CategoryDTO struct {
	gorm.Model
	Name string
}

func getCategoryDTO(categoryDto *CategoryDTO, category Category) {
	categoryDto.ID = category.ID
	categoryDto.Name = category.Name
}

func InitCategoryMigration(db2 *gorm.DB, err2 error) {
	db = db2
	err = err2
	db.AutoMigrate(&Category{})
}

func GetCategory(context echo.Context) error {
	id := context.Param("id")
	var category Category
	db.Find(&category, id)
	var strId = strconv.FormatUint(uint64(category.ID), 10)
	if (strId) != id {
		return context.JSON(http.StatusInternalServerError, "Category not available with id: "+id)
	}
	var categoryDTO CategoryDTO
	getCategoryDTO(&categoryDTO, category)
	return context.JSON(http.StatusOK, &categoryDTO)
}

func GetCategories(context echo.Context) error {
	var categories []Category
	var categoriesDTO []CategoryDTO
	db.Find(&categories)
	if len(categories) == 0 {
		return context.JSON(http.StatusInternalServerError, "There are no categories")
	}

	for i := 0; i < len(categories); i++ {
		var categoryDTO CategoryDTO
		getCategoryDTO(&categoryDTO, categories[i])
		categoriesDTO = append(categoriesDTO, categoryDTO)
	}
	return context.JSON(http.StatusOK, &categoriesDTO)
}

func SaveCategory(context echo.Context) error {
	category := new(Category)
	if err := (&echo.DefaultBinder{}).BindBody(context, &category); err != nil {
		return err
	}
	db.Create(&category)
	return context.JSON(http.StatusOK, &category)
}

func DeleteCategory(context echo.Context) error {
	id := context.Param("id")
	var category Category
	db.First(&category, id)
	var strId = strconv.FormatUint(uint64(category.ID), 10)
	if (strId) != id {
		return context.JSON(http.StatusInternalServerError, "Category not available")
	}
	db.Delete(&category)
	return context.JSON(http.StatusOK, "Category is deleted!!!")
}

func UpdateCategory(context echo.Context) error {
	id := context.Param("id")
	category := new(Category)
	db.First(&category, id)
	var strId = strconv.FormatUint(uint64(category.ID), 10)
	if (strId) != id {
		return context.JSON(http.StatusInternalServerError, "Category not available")
	}

	if err := (&echo.DefaultBinder{}).BindBody(context, &category); err != nil {
		return err
	}
	db.Save(&category)
	return context.JSON(http.StatusOK, &category)
}
