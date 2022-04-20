package com.example.chat.discord

import com.example.chat.command.*
import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.channel.MessageChannel
import discord4j.core.event.domain.message.MessageCreateEvent
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.lang.StringBuilder
import java.util.*

@Component
class HelpCommandHandler : MessageHandler {

    override fun messageReceive(messageCreateEvent: MessageCreateEvent?): Mono<Message?>? {
        val message = messageCreateEvent!!.message
        val resultValue = StringBuilder("ALL COMMANDS:").appendLine()
        val helpCommand = enumValues<HelpCommand>()
        val helloCommand = enumValues<HelloCommand>()
        val timeCommand = enumValues<TimeCommand>()
        val categoryCommand = enumValues<CategoryCommand>()
        val productCommand = enumValues<ProductCommand>()
        resultValue.appendLine().append("HELP COMMANDS:").appendLine()
        helpCommand.forEach { command ->
            resultValue.append(command.help).appendLine()
        }

        resultValue.appendLine().append("HELLO COMMANDS:").appendLine()
        helloCommand.forEach { command ->
            resultValue.append(command.hello).appendLine()
        }

        resultValue.appendLine().append("TIME COMMANDS:").appendLine()
        timeCommand.forEach { command ->
            resultValue.append(command.time).appendLine()
        }

        resultValue.appendLine().append("CATEGORY COMMANDS:").appendLine()
        categoryCommand.forEach { command ->
            resultValue.append(command.category).appendLine()
        }

        resultValue.appendLine().append("PRODUCT COMMANDS:").appendLine()
        productCommand.forEach { command ->
            resultValue.append(command.product).appendLine()
        }

        HelpCommand.values().forEach { msg ->
            if (msg.help.uppercase() == message.content.uppercase(Locale.getDefault())) {
                return message.channel.flatMap { msgChannel: MessageChannel -> msgChannel.createMessage(resultValue.toString()) }
            }
        }
        return null
    }
}