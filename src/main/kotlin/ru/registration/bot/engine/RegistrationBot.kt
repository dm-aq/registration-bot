package ru.registration.bot.engine

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
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

        var chat: Chat?
        var user: User?
        var text: String?

        if (update?.hasCallbackQuery() == true) {
            chat = update.callbackQuery?.message?.chat
            user = update.callbackQuery?.from
            text = update.callbackQuery?.data

            this.execute(SendMessage(chat?.id, text))
        } else {
            chat = update?.message?.chat
            user = update?.message?.from
            text = update?.message?.text
        }

        commonFactory.create(chat, user, this).handle(text)
    }
}
