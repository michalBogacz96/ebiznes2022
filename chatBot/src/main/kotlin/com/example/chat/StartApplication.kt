package com.example.chat

import com.example.chat.annotations.EnableDiscord
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableDiscord
class StartApplication

fun main(args: Array<String>) {
    runApplication<StartApplication>(*args)
}
