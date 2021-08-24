package ru.registration.bot.engine.commands.flow

import org.telegram.telegrambots.meta.api.objects.Update

interface State {
    fun ask(userId: Int, chatId: Long)
    fun handle(update: Update)
}
