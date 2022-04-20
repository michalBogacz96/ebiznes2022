package com.example.chat

import com.example.chat.DiscordClientConfig.AwaitThreadProvider
import discord4j.core.GatewayDiscordClient

abstract class DiscordClientConfig {

    fun awaitThread(gatewayDiscordClient: GatewayDiscordClient): AwaitThreadProvider {
        return AwaitThreadProvider {
            val thread: Thread = object : Thread("discord") {
                override fun run() {
                    gatewayDiscordClient.onDisconnect().block()
                }
            }
            thread.contextClassLoader = javaClass.classLoader
            thread.isDaemon = false
            thread
        }
    }

    fun interface AwaitThreadProvider {
        fun awaitThread(): Thread?
    }
}