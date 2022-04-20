package com.example.chat.discord

import com.example.chat.command.HelloCommand
import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.channel.MessageChannel
import discord4j.core.event.domain.message.MessageCreateEvent
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.*

@Component
class HelloCommandHandler : MessageHandler {

    override fun messageReceive(messageCreateEvent: MessageCreateEvent?): Mono<Message?>? {
        val message = messageCreateEvent!!.message
        HelloCommand.values().forEach { msg ->
            if (msg.hello.uppercase() == message.content.uppercase(Locale.getDefault())) {
                return message.channel.flatMap { msgChannel: MessageChannel -> msgChannel.createMessage("Hello my friend") }
            }
        }
        return null
    }
}