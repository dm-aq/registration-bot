package ru.registration.bot.configuration

import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GoogleConfiguration {

    @Bean
    fun jacksonFactory(): JsonFactory = JacksonFactory.getDefaultInstance()
}
