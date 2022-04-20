package com.example.chat.discord

import com.example.chat.command.ProductCommand
import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.channel.MessageChannel
import discord4j.core.event.domain.message.MessageCreateEvent
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.lang.StringBuilder
import java.util.*

@Component
class ProductCommandHandler : MessageHandler  {

    private val products: List<String> = listOf("Bike", "Bread", "Medicine", "Shirt")

    override fun messageReceive(messageCreateEvent: MessageCreateEvent?): Mono<Message?>? {
        val message = messageCreateEvent!!.message
        val resultValue = StringBuilder("")
        products.forEach { product ->
            resultValue.append(product).appendLine()
        }

        ProductCommand.values().forEach { msg ->
            if (msg.product.uppercase() == message.content.uppercase(Locale.getDefault())) {
                return message.channel.flatMap { msgChannel: MessageChannel ->
                    msgChannel.createMessage(resultValue.toString())
                }
            }
        }
        return null
    }
}