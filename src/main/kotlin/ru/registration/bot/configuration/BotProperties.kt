package ru.registration.bot.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

// todo remove
@ConstructorBinding
@ConfigurationProperties("bot")
data class BotProperties(val username: String, val token: String) {
}