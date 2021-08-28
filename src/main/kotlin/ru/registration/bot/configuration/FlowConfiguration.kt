package ru.registration.bot.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.registration.bot.engine.RegistrationBot
import ru.registration.bot.engine.commands.RemoveDraftCommand
import ru.registration.bot.engine.commands.flow.State
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

@Configuration
class FlowConfiguration {

    @Bean("startState")
    fun startState(
        requestRepository: RequestRepository,
        phoneNumberState: State
    ): State = StartState(requestRepository, phoneNumberState)

    @Bean("phoneNumberState")
    fun phoneNumberState(
        absSender: RegistrationBot,
        stateRepo: StateRepository,
        requestRepository: RequestRepository,
        fullNameState: State
    ): State = PhoneNumberState(absSender, stateRepo, requestRepository, fullNameState)

    @Bean("fullNameState")
    fun fullNameState(
        absSender: RegistrationBot,
        stateRepo: StateRepository,
        requestRepository: RequestRepository,
        mailState: State
    ): State = FullNameState(absSender, stateRepo, requestRepository, mailState)

    @Bean("mailState")
    fun mailState(
        absSender: RegistrationBot,
        stateRepo: StateRepository,
        requestRepository: RequestRepository,
        sexState: State
    ): State = MailState(absSender, stateRepo, requestRepository, sexState)

    @Bean("sexState")
    fun sexState(
        absSender: RegistrationBot,
        stateRepo: StateRepository,
        requestRepository: RequestRepository,
        roomCategoryState: State
    ): State = SexState(absSender, stateRepo, requestRepository, roomCategoryState)

    @Bean("roomCategoryState")
    fun roomCategoryState(
        absSender: RegistrationBot,
        stateRepo: StateRepository,
        requestRepository: RequestRepository,
        roomCategoryProperties: RoomCategoryProperties,
        danceStyleState: State
    ): State = RoomCategoryState(absSender, stateRepo, requestRepository, roomCategoryProperties, danceStyleState)

    @Bean("danceStyleState")
    fun danceStyleState(
        absSender: RegistrationBot,
        stateRepo: StateRepository,
        requestRepository: RequestRepository,
        danceStyleProperties: DanceStyleProperties,
        neighborsState: State
    ): State = DanceStyleState(absSender, stateRepo, requestRepository, danceStyleProperties, neighborsState)

    @Bean("neighborsState")
    fun neighborsState(
        absSender: RegistrationBot,
        stateRepo: StateRepository,
        requestRepository: RequestRepository,
        draftState: State
    ): State = NeighborsState(absSender, stateRepo, requestRepository, draftState)

    @Bean("draftState")
    fun draftState(
        absSender: RegistrationBot,
        stateRepo: StateRepository,
        requestRepository: RequestRepository,
        removeDraftCommand: RemoveDraftCommand,
        exportState: State
    ): State = DraftReadyState(absSender, stateRepo, requestRepository, removeDraftCommand, exportState)

    @Bean("exportState")
    fun exportRequestState(
        absSender: RegistrationBot,
        stateRepo: StateRepository,
        requestRepository: RequestRepository,
        googleSheets: GoogleSheetsService
    ): State = ExportRequestState(absSender, stateRepo, requestRepository, googleSheets)
}
