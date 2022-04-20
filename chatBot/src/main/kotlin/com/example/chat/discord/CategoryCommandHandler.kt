package com.example.chat.discord

import com.example.chat.command.CategoryCommand
import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.channel.MessageChannel
import discord4j.core.event.domain.message.MessageCreateEvent
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.lang.StringBuilder
import java.util.*

@Component
class CategoryCommandHandler : MessageHandler {

    private val categories: List<String> = listOf("Sport", "Food", "Health", "Clothes")

    override fun messageReceive(messageCreateEvent: MessageCreateEvent?): Mono<Message?>? {
        val message = messageCreateEvent!!.message
        val resultValue = StringBuilder("")
        categories.forEach { category ->
            resultValue.append(category).appendLine()
        }

        CategoryCommand.values().forEach { msg ->
            if (msg.category.uppercase() == message.content.uppercase(Locale.getDefault())) {
                return message.channel.flatMap { msgChannel: MessageChannel ->
                    msgChannel.createMessage(resultValue.toString())
                }
            }
        }
        return null
    }
}