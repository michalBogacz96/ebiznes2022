package com.example.chat.annotations

import com.example.chat.DiscordAutoConfiguration
import com.example.chat.beans.DiscordEventBeanProcessor
import org.springframework.context.annotation.Import
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy

@Retention(RetentionPolicy.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Import(DiscordAutoConfiguration::class, DiscordEventBeanProcessor::class)
annotation class EnableDiscord {}