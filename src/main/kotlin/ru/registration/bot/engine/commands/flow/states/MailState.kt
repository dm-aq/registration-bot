package ru.registration.bot.engine.commands.flow.states

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.chatId
import ru.registration.bot.engine.commands.flow.State
import ru.registration.bot.engine.commands.flow.StateType.MAIL_STATE
import ru.registration.bot.engine.text
import ru.registration.bot.engine.userId
import ru.registration.bot.repositories.BotRepository
import ru.registration.bot.repositories.specifications.SetUserStatus
import ru.registration.bot.repositories.specifications.UpdateRequestField

class MailState(
    private val botRepository: BotRepository,
    private val nextState: State
) : State {
    override fun ask(userId: Int, chatId: Long, absSender: AbsSender) {
        botRepository.execute(SetUserStatus(userId, MAIL_STATE))
        absSender.execute(SendMessage(chatId, "Адрес электронной почты:"))
    }

    override fun handle(update: Update, absSender: AbsSender) {
        val text = update.text ?: ""
        if (isValid(text)) {
            botRepository.execute(UpdateRequestField(update.userId, Pair("email", text)))
            nextState.ask(update.userId, update.chatId, absSender)
        } else {
            absSender.execute(SendMessage(update.chatId, "Адрес какой-то не такой :("))
        }
    }

    private fun isValid(text: String): Boolean =
        "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}\$"
            .toRegex(RegexOption.IGNORE_CASE).matches(text)
}
