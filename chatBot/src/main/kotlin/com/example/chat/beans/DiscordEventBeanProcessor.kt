package com.example.chat.beans

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import discord4j.core.GatewayDiscordClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanPostProcessor
import kotlin.Throws
import org.springframework.beans.BeansException
import com.example.chat.annotations.DiscordEventListener
import reactor.core.publisher.Mono
import discord4j.core.event.EventDispatcher
import discord4j.core.event.domain.Event
import org.reactivestreams.Publisher
import org.springframework.stereotype.Component
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Method
import java.util.*


@Component("discordEventBeanProcessor")
@ConditionalOnBean(GatewayDiscordClient::class)
class DiscordEventBeanProcessor @Autowired constructor(var eventDispatcher: EventDispatcher) : BeanPostProcessor {

    private fun doDiscordEventListener(method: Method, bean: Any) {
        val parameterTypes = method.parameterTypes
        if (parameterTypes.isNotEmpty()) {
            if (Event::class.java.isAssignableFrom(parameterTypes[0])) {
                doEvent(method, bean, parameterTypes[0] as Class<out Event?>)
            }
        }
    }

    private fun doEvent(method: Method, bean: Any, parameterClazz: Class<out Event?>) {
        eventDispatcher.on(parameterClazz)
            .flatMap { e: Event? ->
                val r = Optional.ofNullable(ReflectionUtils.invokeMethod(method, bean, e))
                r.filter { o: Any? -> o is Publisher<*> }
                    .map { o: Any? -> o as Publisher<*>? }
                    .orElse(Mono.empty<Any>())
            }
            .doOnError { error: Any? ->
                EventUtils.doOnError(bean, error)
                doEvent(method, bean, parameterClazz)
            }
            .subscribe()
    }

    @Throws(BeansException::class)
    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any? {
        ReflectionUtils.doWithMethods(bean.javaClass) { method: Method ->
            if (method.isAnnotationPresent(
                    DiscordEventListener::class.java
                )
            ) {
                doDiscordEventListener(method, bean)
            }
        }
        return bean
    }
}