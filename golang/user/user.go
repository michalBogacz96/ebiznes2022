package user

import (
	"github.com/labstack/echo/v4"
	"golang/role"
	"gorm.io/gorm"
	"net/http"
	"strconv"
)

var db *gorm.DB
var err error

type User struct {
	gorm.Model
	FirstName  string
	SecondName string
	Role       role.Role
	RoleID     uint
}

type UserDTO struct {
	ID         uint
	FirstName  string
	SecondName string
	RoleID     uint
}

func InitUserMigration(db2 *gorm.DB, err2 error) {
	db = db2
	err = err2
	db.AutoMigrate(&User{})
}

func getUserDto(userDto *UserDTO, user User) {
	userDto.ID = user.ID
	userDto.FirstName = user.FirstName
	userDto.SecondName = user.SecondName
	userDto.RoleID = user.RoleID
}

func GetUser(context echo.Context) error {
	id := context.Param("id")
	var user User
	db.Find(&user, id)
	var strId = strconv.FormatUint(uint64(user.ID), 10)
	if (strId) != id {
		return context.JSON(http.StatusInternalServerError, "User not available with id: "+id)
	}
	var userDto UserDTO
	getUserDto(&userDto, user)
	return context.JSON(http.StatusOK, &userDto)
}

func GetUsers(context echo.Context) error {
	var users []User
	var usersDto []UserDTO
	db.Find(&users)
	if len(users) == 0 {
		return context.JSON(http.StatusInternalServerError, "There are no users")
	}

	for i := 0; i < len(users); i++ {
		var userDto UserDTO
		getUserDto(&userDto, users[i])
		usersDto = append(usersDto, userDto)
	}

	return context.JSON(http.StatusOK, &usersDto)
}

func SaveUser(context echo.Context) error {
	user := new(User)
	if err := (&echo.DefaultBinder{}).BindBody(context, &user); err != nil {
		return err
	}
	db.Create(&user).Association("Role")
	var strId = strconv.FormatUint(uint64(user.ID), 10)
	return context.JSON(http.StatusOK, "user added with id: "+strId)
}

func DeleteUser(context echo.Context) error {
	id := context.Param("id")
	var user User
	db.First(&user, id)
	var strId = strconv.FormatUint(uint64(user.ID), 10)
	if (strId) != id {
		return context.JSON(http.StatusInternalServerError, "User not available")
	}
	db.Delete(&user)
	return context.JSON(http.StatusOK, "User is deleted!!!")
}

func UpdateUser(context echo.Context) error {
	id := context.Param("id")
	user := new(User)
	db.First(&user, id)
	var strId = strconv.FormatUint(uint64(user.ID), 10)
	if (strId) != id {
		return context.JSON(http.StatusInternalServerError, "User not available")
	}

	if err := (&echo.DefaultBinder{}).BindBody(context, &user); err != nil {
		return err
	}
	db.Save(&user)
	return context.JSON(http.StatusOK, &user)
}