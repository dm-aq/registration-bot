package ru.registration.bot.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("room")
data class RoomCategoryProperties(val categories: Map<Int, Category>)

data class Category(val price: String, val description: String)