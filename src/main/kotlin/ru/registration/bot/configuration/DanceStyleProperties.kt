package ru.registration.bot.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("dance-styles")
data class DanceStyleProperties(val values: List<String>)
