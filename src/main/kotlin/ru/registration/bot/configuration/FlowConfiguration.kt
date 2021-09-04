package ru.registration.bot.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.registration.bot.engine.commands.RemoveDraftComponent
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
        stateRepo: StateRepository,
        requestRepository: RequestRepository,
        fullNameState: State
    ): State = PhoneNumberState(stateRepo, requestRepository, fullNameState)

    @Bean("fullNameState")
    fun fullNameState(
        stateRepo: StateRepository,
        requestRepository: RequestRepository,
        mailState: State
    ): State = FullNameState(stateRepo, requestRepository, mailState)

    @Bean("mailState")
    fun mailState(
        stateRepo: StateRepository,
        requestRepository: RequestRepository,
        sexState: State
    ): State = MailState(stateRepo, requestRepository, sexState)

    @Bean("sexState")
    fun sexState(
        stateRepo: StateRepository,
        requestRepository: RequestRepository,
        roomCategoryState: State
    ): State = SexState(stateRepo, requestRepository, roomCategoryState)

    @Bean("roomCategoryState")
    fun roomCategoryState(
        stateRepo: StateRepository,
        requestRepository: RequestRepository,
        roomCategoryProperties: RoomCategoryProperties,
        danceStyleState: State
    ): State = RoomCategoryState(stateRepo, requestRepository, roomCategoryProperties, danceStyleState)

    @Bean("danceStyleState")
    fun danceStyleState(
        stateRepo: StateRepository,
        requestRepository: RequestRepository,
        danceStyleProperties: DanceStyleProperties,
        neighborsState: State
    ): State = DanceStyleState(stateRepo, requestRepository, danceStyleProperties, neighborsState)

    @Bean("neighborsState")
    fun neighborsState(
        stateRepo: StateRepository,
        requestRepository: RequestRepository,
        draftState: State
    ): State = NeighborsState(stateRepo, requestRepository, draftState)

    @Bean("draftState")
    fun draftState(
        stateRepo: StateRepository,
        requestRepository: RequestRepository,
        removeDraftComponent: RemoveDraftComponent,
        exportState: State
    ): State = DraftReadyState(stateRepo, requestRepository, removeDraftComponent, exportState)

    @Bean("exportState")
    fun exportRequestState(
        stateRepo: StateRepository,
        requestRepository: RequestRepository,
        googleSheets: GoogleSheetsService
    ): State = ExportRequestState(stateRepo, requestRepository, googleSheets)
}
