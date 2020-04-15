package ru.registration.bot.engine.commands.flow

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.bots.AbsSender

class EmptyState(
    private val chat: Chat?,
    private val absSender: AbsSender?
    ): State {
    override fun ask() {
        absSender?.execute(SendMessage(chat?.id, "EmptyState -> ask()"))
    }

    override fun handle(text: String?) {
        absSender?.execute(SendMessage(chat?.id, "EmptyState -> handle()"))
    }
}
