package ru.registration.bot.engine

import org.springframework.stereotype.Component
import ru.registration.bot.engine.commands.flow.State
import ru.registration.bot.engine.commands.flow.StateType
import ru.registration.bot.engine.commands.flow.StateType.START_STATE
import ru.registration.bot.repositories.StateRepository
import ru.registration.bot.repositories.specifications.CurrentUserState

@Component
class CommonFactory(
    private val stateRepo: StateRepository,
    private val states: Map<String, State>
) {

    fun create(userId: Int) =
        states[currentUserStateType(userId).beanName]

    fun currentUserStateType(userId: Int): StateType =
        stateRepo.query(CurrentUserState(userId)).firstOrNull() ?: START_STATE
}
