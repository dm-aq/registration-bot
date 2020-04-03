package ru.registration.bot.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("rooms")
data class RoomCategoryProperties(val prices: Map<Int, String>)