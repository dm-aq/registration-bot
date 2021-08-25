package ru.registration.bot.engine.commands.flow.states

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.engine.chatId
import ru.registration.bot.engine.commands.flow.State
import ru.registration.bot.engine.commands.flow.StateType.NEIGHBORS_STATE
import ru.registration.bot.engine.text
import ru.registration.bot.engine.userId
import ru.registration.bot.repositories.specifications.SetUserStatus
import ru.registration.bot.repositories.specifications.UpdateRequestField

class NeighborsState(
    private val absSender: AbsSender?,
    private val commonFactory: CommonFactory,
    private val nextState: State
) : State {
    override fun ask(userId: Int, chatId: Long) {
        commonFactory.stateRepo.execute(SetUserStatus(userId, NEIGHBORS_STATE))
        absSender?.execute(SendMessage(chatId, "С кем будете жить"))
    }

    override fun handle(update: Update) {
        val text = update.text ?: ""
        if (text.length <= 120) {
            commonFactory.requestRepository.execute(UpdateRequestField(update.userId, Pair("neighbors", text)))
            nextState.ask(update.userId, update.chatId)
        } else {
            absSender?.execute(SendMessage(update.chatId, "Слишком много букв. Будьте скромнее."))
        }
    }
}
