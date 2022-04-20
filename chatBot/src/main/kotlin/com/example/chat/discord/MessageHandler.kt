package com.example.chat.discord

import discord4j.core.`object`.entity.Message
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono

interface MessageHandler {
    fun messageReceive(messageCreateEvent: MessageCreateEvent?): Mono<Message?>?
}