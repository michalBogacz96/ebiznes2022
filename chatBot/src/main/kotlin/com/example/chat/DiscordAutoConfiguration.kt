package com.example.chat

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import com.example.chat.annotations.EnableDiscord
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import discord4j.core.DiscordClient
import discord4j.core.DiscordClientBuilder
import org.springframework.context.annotation.DependsOn
import discord4j.core.GatewayDiscordClient
import discord4j.core.event.EventDispatcher

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    DiscordTokenConfiguration::class
)
@ConditionalOnBean(annotation = [EnableDiscord::class])
class DiscordAutoConfiguration : DiscordClientConfig() {


    @Bean("eventDispatcher")
    @ConditionalOnMissingBean
    fun eventDispatcher(): EventDispatcher {
        return EventDispatcher.builder().build()
    }

    @Bean("discordClient")
    @ConditionalOnMissingBean
    fun discordClient(tokenProvider: DiscordTokenProvider): DiscordClient {
        return DiscordClientBuilder.create(tokenProvider.getToken()!!).build()
    }

    @Bean
    @DependsOn("discordClient", "eventDispatcher", "discordEventBeanProcessor")
    @ConditionalOnMissingBean
    fun gatewayDiscordClient(discordClient: DiscordClient, eventDispatcher: EventDispatcher?): GatewayDiscordClient? {
        val gateway = discordClient.gateway()
        return gateway
            .setEventDispatcher(eventDispatcher)
            .login()
            .doOnNext { t: GatewayDiscordClient? ->
                awaitThread(t!!).awaitThread()!!
                    .start()
            }
            .block()
    }
}