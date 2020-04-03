package ru.registration.bot

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.telegram.telegrambots.ApiContextInitializer
import ru.registration.bot.configuration.BotProperties
import ru.registration.bot.configuration.RoomCategoryProperties

@EnableConfigurationProperties(
    value = [BotProperties::class, RoomCategoryProperties::class]
)
@SpringBootApplication
class MainApplication

fun main(args: Array<String>){
    ApiContextInitializer.init()
    runApplication<MainApplication>(*args){
        setBannerMode(Banner.Mode.OFF)
    }
}

