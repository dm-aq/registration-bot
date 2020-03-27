package ru.registration.bot.engine

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot
import org.telegram.telegrambots.meta.api.objects.Update
import ru.registration.bot.engine.commands.RegistrationCommand
import ru.registration.bot.engine.commands.RemoveDraftCommand
import ru.registration.bot.engine.commands.StartCommand

@Component
open class RegistrationBot(
    @Value("\${bot.token}") private val token: String,
    private val commonFactory: CommonFactory): TelegramLongPollingCommandBot() {

    init {
        register(StartCommand(commonFactory))
        register(RegistrationCommand(commonFactory))
        register(RemoveDraftCommand(commonFactory))
    }

    override fun getBotUsername(): String {
        return "IvaraBot"
    }

    override fun getBotToken(): String {
        return token
    }

    override fun processNonCommandUpdate(update: Update?) {

        commonFactory.create(update?.message?.chat, update?.message?.from, this).handle(update?.message?.text)
    }
}