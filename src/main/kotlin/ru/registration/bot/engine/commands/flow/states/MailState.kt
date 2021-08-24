package ru.registration.bot.engine.commands.flow.states

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.engine.chatId
import ru.registration.bot.engine.commands.flow.State
import ru.registration.bot.engine.commands.flow.StateType.FULL_NAME_STATE
import ru.registration.bot.engine.commands.flow.StateType.MAIL_STATE
import ru.registration.bot.engine.text
import ru.registration.bot.engine.userId
import ru.registration.bot.repositories.specifications.SetUserStatus
import ru.registration.bot.repositories.specifications.UpdateRequestField

class MailState(
    private val chat: Chat?,
    private val user: User?,
    private val absSender: AbsSender?,
    private val commonFactory: CommonFactory
) : State {
    override fun ask(userId: Int, chatId: Long) {
        commonFactory.stateRepo.execute(SetUserStatus(userId, FULL_NAME_STATE, MAIL_STATE))
        absSender?.execute(SendMessage(chatId, "Адрес электронной почты:"))
    }

    override fun handle(update: Update) {
        val text = update.text ?: ""
        if (validate(text)) {
            commonFactory.requestRepository.execute(UpdateRequestField(update.userId, Pair("email", text)))
            SexState(chat, user, absSender, commonFactory).ask(update.userId, update.chatId)
        }
    }

    private fun validate(text: String): Boolean {
        if (!"^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}\$"
                .toRegex(RegexOption.IGNORE_CASE).matches(text)) {
            absSender?.execute(SendMessage(chat?.id, "Адрес какой-то не такой :("))
            return false
        }
        return true
    }
}
