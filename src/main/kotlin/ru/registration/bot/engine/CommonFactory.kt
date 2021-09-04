package ru.registration.bot.engine

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
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

    fun create(chat: Chat, user: User, absSender: AbsSender, text: String? = null) =
        states[currentUserStateType(user).beanName]

    fun currentUserStateType(user: User): StateType =
        stateRepo.query(CurrentUserState(user.id)).firstOrNull() ?: START_STATE
}
