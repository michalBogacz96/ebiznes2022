package com.example.chat.discord

import com.example.chat.command.TimeCommand
import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.channel.MessageChannel
import discord4j.core.event.domain.message.MessageCreateEvent
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.text.SimpleDateFormat
import java.util.*

@Component
class TimeCommandHandler : MessageHandler {

    override fun messageReceive(messageCreateEvent: MessageCreateEvent?): Mono<Message?>? {
        val message = messageCreateEvent!!.message
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
        TimeCommand.values().forEach { msg ->
            if (msg.time.uppercase() == message.content.uppercase(Locale.getDefault())) {
                return message.channel.flatMap { msgChannel: MessageChannel -> msgChannel.createMessage(date.toString()) }
            }
        }
        return null
    }
}