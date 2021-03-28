package ru.registration.bot.engine.commands

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.CommonFactory

class StartCommand(
    private val commonFactory: CommonFactory
) : BotCommand("/start", "Start new registration") {

    override fun execute(absSender: AbsSender?, user: User?, chat: Chat?, arguments: Array<out String>?) {

        absSender?.execute(SendMessage(chat?.id, """
            12-13 июня состоится наш ежегодный #ивараВыезд. Я помогу вам зарегистрироваться.
        """.trimIndent())
            .setReplyMarkup(registrationButton())
        )
    }

    private fun registrationButton(): InlineKeyboardMarkup =
        InlineKeyboardMarkup().setKeyboard(
            listOf(
                listOf(
                    InlineKeyboardButton()
                        .setText("Начать регистрацию")
                        .setCallbackData("new_registration")
                )
            )
        )
}
