package ru.registration.bot.engine.commands.flow.states

import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.chatId
import ru.registration.bot.engine.commands.flow.State
import ru.registration.bot.engine.commands.flow.StateType.START_STATE
import ru.registration.bot.engine.text
import ru.registration.bot.engine.userId
import ru.registration.bot.repositories.RequestRepository
import ru.registration.bot.repositories.specifications.InitUserRequest

class StartState(
    private val requestRepository: RequestRepository,
    private val nextState: State
) : State {

    override fun ask(userId: Int, chatId: Long, absSender: AbsSender) {
        requestRepository.execute(InitUserRequest(userId, START_STATE))

        nextState.ask(userId, chatId, absSender)
    }

    override fun handle(update: Update, absSender: AbsSender) {
        if (update.text == "new_registration") {
            ask(update.userId, update.chatId, absSender)
        }
    }
}
