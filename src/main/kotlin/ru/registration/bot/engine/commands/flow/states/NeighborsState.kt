package ru.registration.bot.engine.commands.flow.states

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.engine.chatId
import ru.registration.bot.engine.commands.flow.State
import ru.registration.bot.engine.commands.flow.StateType.DANCESTYLE_STATE
import ru.registration.bot.engine.commands.flow.StateType.NEIGHBORS_STATE
import ru.registration.bot.engine.text
import ru.registration.bot.engine.userId
import ru.registration.bot.repositories.specifications.SetUserStatus
import ru.registration.bot.repositories.specifications.UpdateRequestField

class NeighborsState(
    private val chat: Chat?,
    private val user: User?,
    private val absSender: AbsSender?,
    private val commonFactory: CommonFactory
) : State {
    override fun ask(userId: Int, chatId: Long) {
        commonFactory.stateRepo.execute(SetUserStatus(userId, DANCESTYLE_STATE, NEIGHBORS_STATE))
        absSender?.execute(SendMessage(chatId, "С кем будете жить"))
    }

    override fun handle(update: Update) {
        val text = update.text ?: ""
        if (validate(text)) {
            commonFactory.requestRepository.execute(UpdateRequestField(update.userId, Pair("neighbors", text)))
            DraftReadyState(chat, user, absSender, commonFactory).ask(update.userId, update.chatId)
        }
    }

    private fun validate(text: String): Boolean {
        if (text.length > 120) {
            absSender?.execute(SendMessage(chat?.id, "Слишком много букв. Будьте скромнее."))
            return false
        }

        return true
    }
}
