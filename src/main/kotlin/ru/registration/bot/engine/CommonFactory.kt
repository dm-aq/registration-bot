package ru.registration.bot.engine

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.commands.flow.*
import ru.registration.bot.repositories.RequestRepository
import ru.registration.bot.repositories.StateRepository
import ru.registration.bot.repositories.specifications.CurrentUserState

@Component
class CommonFactory(
    val stateRepo: StateRepository,
    val requestRepository: RequestRepository
) {

    fun create(chat: Chat?, user: User?, absSender: AbsSender?, text: String? = null) =
        when(currentUserStateType(user)){
            StateType.START_STATE -> StartState(chat, user, absSender, this)
            StateType.PHONE_STATE -> PhoneNumberState(chat, user, absSender, this)
            StateType.FULL_NAME_STATE -> FullNameState(chat, user, absSender, this)
            StateType.SEX_STATE -> SexState(chat, user, absSender, this)
            StateType.ROOM_STATE -> RoomCategoryState(chat, user, absSender, this)
            StateType.DANCESTYLE_STATE -> DanceStyleState(chat, user, absSender, this)
            StateType.NEIGHBORS_STATE -> NeighborsState(chat, user, absSender, this)
            StateType.REQUEST_READY -> ExportRequestState(chat, user, absSender, this)
            StateType.IMPORTED -> ExportRequestState(chat, user, absSender, this)
            else -> EmptyState(chat, absSender)
        }

    fun currentUserStateType(user: User?) =
        stateRepo.query(CurrentUserState(user?.id)).firstOrNull() ?: StateType.START_STATE
}