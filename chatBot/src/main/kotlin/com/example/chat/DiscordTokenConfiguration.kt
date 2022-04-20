package com.example.chat

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean

class DiscordTokenConfiguration(
    @Value("\${token}")
    private val token: String
) {

    companion object {
        private val logger = LoggerFactory.getLogger(DiscordTokenConfiguration::class.java)
    }

    @Bean("discordTokenProvider")
    @ConditionalOnMissingBean
    fun tokenProvider(): DiscordTokenProvider {
        logger.debug("token {}.", token)
        return DiscordTokenProvider { token }
    }
}