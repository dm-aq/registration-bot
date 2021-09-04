package ru.registration.bot.engine.commands.flow.states

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.chatId
import ru.registration.bot.engine.commands.flow.State
import ru.registration.bot.engine.commands.flow.StateType.NEIGHBORS_STATE
import ru.registration.bot.engine.text
import ru.registration.bot.engine.userId
import ru.registration.bot.repositories.RequestRepository
import ru.registration.bot.repositories.StateRepository
import ru.registration.bot.repositories.specifications.SetUserStatus
import ru.registration.bot.repositories.specifications.UpdateRequestField

class NeighborsState(
    private val stateRepo: StateRepository,
    private val requestRepository: RequestRepository,
    private val nextState: State
) : State {
    override fun ask(userId: Int, chatId: Long, absSender: AbsSender) {
        stateRepo.execute(SetUserStatus(userId, NEIGHBORS_STATE))
        absSender.execute(SendMessage(chatId, "С кем будете жить"))
    }

    override fun handle(update: Update, absSender: AbsSender) {
        val text = update.text ?: ""
        if (text.length <= 120) {
            requestRepository.execute(UpdateRequestField(update.userId, Pair("neighbors", text)))
            nextState.ask(update.userId, update.chatId, absSender)
        } else {
            absSender.execute(SendMessage(update.chatId, "Слишком много букв. Будьте скромнее."))
        }
    }
}