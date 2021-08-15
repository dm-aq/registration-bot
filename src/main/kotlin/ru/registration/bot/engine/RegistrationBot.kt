package ru.registration.bot.engine

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update
import ru.registration.bot.BaseTelegramLongPollingBot
import ru.registration.bot.RegistrationBotCommand

@Component
class RegistrationBot(
    @Value("\${bot.username}") private val botName: String,
    @Value("\${bot.token}") private val token: String,
    private val commonFactory: CommonFactory,
    commands: List<RegistrationBotCommand>
) : BaseTelegramLongPollingBot(botName, token) {

    init {
        commands.forEach { register(it) }
    }

    override fun processNonCommandUpdate(update: Update?) {

        commonFactory.create(update?.chat, update?.user, this).handle(update)
    }
}
