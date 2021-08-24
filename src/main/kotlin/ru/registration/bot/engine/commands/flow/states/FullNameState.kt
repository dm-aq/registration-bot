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
import ru.registration.bot.engine.commands.flow.StateType.PHONE_STATE
import ru.registration.bot.engine.text
import ru.registration.bot.engine.userId
import ru.registration.bot.repositories.specifications.SetUserStatus
import ru.registration.bot.repositories.specifications.UpdateRequestField

class FullNameState(
    private val chat: Chat?,
    private val user: User?,
    private val absSender: AbsSender?,
    private val commonFactory: CommonFactory
) : State {
    override fun ask(userId: Int, chatId: Long) {
        commonFactory.stateRepo.execute(SetUserStatus(userId, PHONE_STATE, FULL_NAME_STATE))
        absSender?.execute(SendMessage(chatId, "Как вас зовут (фио):"))
    }

    override fun handle(update: Update) {
        val text = update?.text ?: ""
        if (validate(text)) {
            commonFactory.requestRepository.execute(UpdateRequestField(user?.id, Pair("full_name", text)))
            MailState(chat, user, absSender, commonFactory).ask(update.userId, update.chatId)
        }
    }

    private fun validate(text: String?): Boolean {
        if (text.isNullOrEmpty() || text.isBlank()) {
            absSender?.execute(SendMessage(chat?.id, "Что-то не так. Тут нет вашего имени."))
            return false
        } else if (text.length > 30) {
            absSender?.execute(SendMessage(chat?.id, "Имя слишком длинное."))
            return false
        }

        return true
    }
}
