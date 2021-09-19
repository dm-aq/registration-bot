package ru.registration.bot.engine.commands.flow

import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender

interface State {
    fun ask(userId: Int, chatId: Long, absSender: AbsSender)
    fun handle(update: Update, absSender: AbsSender)
}
