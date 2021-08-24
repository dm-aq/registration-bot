package ru.registration.bot.engine.commands.flow.states

import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.engine.chatId
import ru.registration.bot.engine.commands.flow.Node
import ru.registration.bot.engine.commands.flow.PersistedState
import ru.registration.bot.engine.commands.flow.StateType.START_STATE
import ru.registration.bot.engine.text
import ru.registration.bot.engine.userId
import ru.registration.bot.repositories.specifications.InitUserRequest

class StartState(
    private val user: User?,
    private val commonFactory: CommonFactory,
    nextState: PersistedState
) : PersistedState, Node<PersistedState> {

    override val prev: PersistedState? = null

    override val next = nextState

    override val type = START_STATE

    override fun ask(userId: Int, chatId: Long) {
        commonFactory.requestRepository.execute(InitUserRequest(user, type))

        next.ask(userId, chatId)
    }

    override fun handle(update: Update) {
        if (update.text == "new_registration") {
            ask(update.userId, update.chatId)
        }
    }
}
