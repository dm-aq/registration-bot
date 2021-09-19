package ru.registration.bot

import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.stereotype.Service
import java.util.Locale

@Service
class MessageService(
    private val messageSource: ResourceBundleMessageSource,
    private val locale: Locale
) {
    fun getMessage(code: String) =
        messageSource.getMessage(code, null, locale)
}
