package ru.registration.bot.engine

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.configuration.DanceStyleProperties
import ru.registration.bot.configuration.RoomCategoryProperties
import ru.registration.bot.engine.commands.flow.State
import ru.registration.bot.engine.commands.flow.StateType
import ru.registration.bot.engine.commands.flow.states.DanceStyleState
import ru.registration.bot.engine.commands.flow.states.DraftReadyState
import ru.registration.bot.engine.commands.flow.states.ExportRequestState
import ru.registration.bot.engine.commands.flow.states.FullNameState
import ru.registration.bot.engine.commands.flow.states.MailState
import ru.registration.bot.engine.commands.flow.states.NeighborsState
import ru.registration.bot.engine.commands.flow.states.PhoneNumberState
import ru.registration.bot.engine.commands.flow.states.RoomCategoryState
import ru.registration.bot.engine.commands.flow.states.SexState
import ru.registration.bot.engine.commands.flow.states.StartState
import ru.registration.bot.google.GoogleSheetsService
import ru.registration.bot.repositories.RequestRepository
import ru.registration.bot.repositories.StateRepository
import ru.registration.bot.repositories.specifications.CurrentUserState

@Component
class CommonFactory(
    val googleSheets: GoogleSheetsService,
    val roomCategoryProperties: RoomCategoryProperties,
    val danceStyleProperties: DanceStyleProperties,
    val stateRepo: StateRepository,
    val requestRepository: RequestRepository
) {

    fun create(chat: Chat?, user: User?, absSender: AbsSender?, text: String? = null) =
        when (currentUserStateType(user)) {
            StateType.START_STATE -> initialState(chat, user, absSender)
            StateType.PHONE_STATE -> PhoneNumberState(chat, user, absSender, this)
            StateType.FULL_NAME_STATE -> FullNameState(chat, user, absSender, this)
            StateType.MAIL_STATE -> MailState(chat, user, absSender, this)
            StateType.SEX_STATE -> SexState(chat, user, absSender, this)
            StateType.ROOM_STATE -> RoomCategoryState(chat, user, absSender, this)
            StateType.DANCESTYLE_STATE -> DanceStyleState(chat, user, absSender, this)
            StateType.NEIGHBORS_STATE -> NeighborsState(chat, user, absSender, this)
            StateType.REQUEST_READY -> DraftReadyState(chat, user, absSender, this)
            StateType.REQUEST_APPROVED -> ExportRequestState(chat, user, absSender, this)
            StateType.EXPORTED -> ExportRequestState(chat, user, absSender, this)
        }

    fun currentUserStateType(user: User?) =
        stateRepo.query(CurrentUserState(user?.id)).firstOrNull() ?: StateType.START_STATE

    private fun initialState(chat: Chat?, user: User?, absSender: AbsSender?): State =
        StartState(
            user,
            this,
            PhoneNumberState(chat, user, absSender, this)
        )
}
