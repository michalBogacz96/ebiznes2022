package com.example.chat.command

enum class ProductCommand(val product: String) {

    PRODUCT("!product"),
    PRODUCTS("!products"),
    WHAT_PRODUCTS_ARE("!What products are"),
    SHOW_PRODUCTS("!Show products")
}