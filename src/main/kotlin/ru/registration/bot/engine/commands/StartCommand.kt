package ru.registration.bot.engine.commands

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.CommonFactory

class StartCommand(
    private val commonFactory: CommonFactory
) : BotCommand("/start", "Start new registration") {

    override fun execute(absSender: AbsSender?, user: User?, chat: Chat?, arguments: Array<out String>?) {

        absSender?.execute(SendMessage(chat?.id, """
            11-12 июля состоится <событие>. Я помогу вам зарегистрироваться.
            /new_registration - зарегистрироваться
        """.trimIndent()))
    }
}
