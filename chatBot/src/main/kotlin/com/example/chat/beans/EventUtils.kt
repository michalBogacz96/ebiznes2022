package com.example.chat.beans

import com.example.chat.annotations.DiscordErrorHandler
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Method
import java.util.*

object EventUtils {

    fun doOnError(bean: Any, error: Any?) {
        val errorHandler = Arrays.stream(bean.javaClass.declaredMethods)
            .filter { m: Method ->
                m.isAnnotationPresent(
                    DiscordErrorHandler::class.java
                )
            }
            .findFirst()
        errorHandler.ifPresent { m: Method? ->
            ReflectionUtils.invokeMethod(
                m!!, bean, error
            )
        }
    }
}