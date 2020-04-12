package ru.registration.bot.engine.commands.flow

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.repositories.specifications.SetUserStatus
import ru.registration.bot.repositories.specifications.UpdateRequestField

class NeighborsState(
    private val chat: Chat?,
    private val user: User?,
    private val absSender: AbsSender?,
    private val commonFactory: CommonFactory
): State {
    override fun ask() {
        commonFactory.stateRepo.execute(SetUserStatus(user?.id, StateType.DANCESTYLE_STATE, StateType.NEIGHBORS_STATE))
        absSender?.execute(SendMessage(chat?.id, "С кем будуте жить:"))
    }

    override fun handle(text: String?) {
        if (validate(text ?: "")) {
            commonFactory.requestRepository.execute(UpdateRequestField(user?.id, Pair("neighbors", text ?: "")))
            DraftReadyState(chat, user, absSender, commonFactory).ask()
        }
    }

    private fun validate(text: String): Boolean {
        if(text.length > 120){
            absSender?.execute(SendMessage(chat?.id, "Слишком много букв. Будь скромнее."))
            return false
        }

        return true
    }
}
