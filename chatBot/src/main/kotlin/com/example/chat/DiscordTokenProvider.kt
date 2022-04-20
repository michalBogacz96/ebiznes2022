package com.example.chat

fun interface DiscordTokenProvider {
    fun getToken(): String?
}