package shop.entity

data class User(

    val id: Long,

    val firstName: String,

    val lastName: String,

    var email: String,

    var password: String
)
