package ru.registration.bot.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import java.util.Locale

@Configuration
class LocaleConfiguration {

    @Bean
    fun locale(@Value("\${locale:ru}") code: String) = Locale(code)

    @Bean
    fun messageSource(): ResourceBundleMessageSource =
        ResourceBundleMessageSource().apply {
            this.setBasename("messages")
            this.setDefaultEncoding("UTF-8")
        }
}
