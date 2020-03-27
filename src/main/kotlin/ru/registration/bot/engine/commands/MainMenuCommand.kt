package ru.registration.bot.engine.commands

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import org.telegram.telegrambots.meta.bots.AbsSender
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.util.*

class MainMenuCommand : BotCommand("/main", "Show main menu"){

    override fun execute(absSender: AbsSender?, user: User?, chat: Chat?, arguments: Array<out String>?) {

        val sendMessage = SendMessage()
        sendMessage.enableMarkdown(true)
        sendMessage.setChatId(chat?.id)
        sendMessage.replyMarkup = createMainMenuKeyboard()
        sendMessage.text = "Регистрация"

        try {
            absSender?.execute(sendMessage)
        }catch (e: TelegramApiException){
//            BotLogger.error("MAINMENUCOMMAND", e)
        }
    }

    fun createMainMenuKeyboard(): ReplyKeyboardMarkup {
        val replyKeyboardMarkup = ReplyKeyboardMarkup()
        replyKeyboardMarkup.selective = true
        replyKeyboardMarkup.resizeKeyboard = true
        replyKeyboardMarkup.oneTimeKeyboard = false

        val keyboard: MutableList<KeyboardRow> = ArrayList()
        val keyboardFirstRow = KeyboardRow()
        keyboardFirstRow.add("Registration")
        keyboard.add(keyboardFirstRow)
        replyKeyboardMarkup.keyboard = keyboard

        return replyKeyboardMarkup
    }

}