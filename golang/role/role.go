package role

import (
	"github.com/labstack/echo/v4"
	"gorm.io/gorm"
	"net/http"
	"strconv"
)

var db *gorm.DB
var err error

type Role struct {
	gorm.Model
	Name string
}

func InitRoleMigration(db2 *gorm.DB, err2 error) {
	db = db2
	err = err2
	db.AutoMigrate(&Role{})
}

func GetRole(context echo.Context) error {
	id := context.Param("id")
	var role Role
	db.Find(&role, id)
	var strId = strconv.FormatUint(uint64(role.ID), 10)
	if (strId) != id {
		return context.JSON(http.StatusInternalServerError, "Role not available with id: "+id)
	}
	return context.JSON(http.StatusOK, &role)
}

func GetRoles(context echo.Context) error {
	var roles []Role
	db.Find(&roles)

	return context.JSON(http.StatusOK, &roles)
}

func SaveRole(context echo.Context) error {
	role := new(Role)
	if err := (&echo.DefaultBinder{}).BindBody(context, &role); err != nil {
		return err
	}
	db.Create(&role)
	return context.JSON(http.StatusOK, &role)
}

func DeleteRole(context echo.Context) error {
	id := context.Param("id")
	var role Role
	db.First(&role, id)
	var strId = strconv.FormatUint(uint64(role.ID), 10)
	if (strId) != id {
		return context.JSON(http.StatusInternalServerError, "Role not available")
	}
	db.Delete(&role)
	return context.JSON(http.StatusOK, "Role is deleted!!!")
}

func UpdateRole(context echo.Context) error {
	id := context.Param("id")
	role := new(Role)
	db.First(&role, id)
	var strId = strconv.FormatUint(uint64(role.ID), 10)
	if (strId) != id {
		return context.JSON(http.StatusInternalServerError, "Role not available")
	}

	if err := (&echo.DefaultBinder{}).BindBody(context, &role); err != nil {
		return err
	}
	db.Save(&role)
	return context.JSON(http.StatusOK, &role)
}
