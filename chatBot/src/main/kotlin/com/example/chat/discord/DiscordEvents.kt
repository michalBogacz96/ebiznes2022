package com.example.chat.discord

import com.example.chat.annotations.DiscordErrorHandler
import org.springframework.beans.factory.annotation.Autowired
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import com.example.chat.annotations.DiscordEventListener
import discord4j.core.`object`.entity.Message
import discord4j.core.event.domain.lifecycle.ConnectEvent
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono

@Component
class DiscordEvents @Autowired constructor(private val messageHandlers: List<MessageHandler>) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(DiscordEvents::class.java)
    }

    @DiscordEventListener
    fun handleConnect(connectEvent: ConnectEvent?) {
        LOGGER.info("Connected to Discord.")
    }

    @DiscordEventListener
    fun messageReceived(messageCreateEvent: MessageCreateEvent?): Mono<Message?> {
        for (handler in messageHandlers) {
            val messageToReturn = handler.messageReceive(messageCreateEvent)
            if (messageToReturn != null) {
                return messageToReturn
            }
        }

        return Mono.empty()
    }

    @DiscordErrorHandler
    fun onError(e: Throwable?) {
        println("Error!")
    }
}