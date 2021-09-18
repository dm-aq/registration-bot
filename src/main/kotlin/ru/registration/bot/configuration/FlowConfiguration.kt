package ru.registration.bot.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.registration.bot.engine.commands.RemoveDraftComponent
import ru.registration.bot.engine.commands.flow.State
import ru.registration.bot.engine.commands.flow.states.DanceStyleState
import ru.registration.bot.engine.commands.flow.states.DraftReadyState
import ru.registration.bot.engine.commands.flow.states.ExportRequestState
import ru.registration.bot.engine.commands.flow.states.FullNameState
import ru.registration.bot.engine.commands.flow.states.GenderState
import ru.registration.bot.engine.commands.flow.states.MailState
import ru.registration.bot.engine.commands.flow.states.NeighborsState
import ru.registration.bot.engine.commands.flow.states.PhoneNumberState
import ru.registration.bot.engine.commands.flow.states.RoomCategoryState
import ru.registration.bot.engine.commands.flow.states.StartState
import ru.registration.bot.google.GoogleSheetsService
import ru.registration.bot.repositories.BotRepository

@Configuration
class FlowConfiguration {

    @Bean("startState")
    fun startState(
        botRepository: BotRepository,
        phoneNumberState: State
    ): State = StartState(botRepository, phoneNumberState)

    @Bean("phoneNumberState")
    fun phoneNumberState(
        botRepository: BotRepository,
        fullNameState: State
    ): State = PhoneNumberState(botRepository, fullNameState)

    @Bean("fullNameState")
    fun fullNameState(
        botRepository: BotRepository,
        mailState: State
    ): State = FullNameState(botRepository, mailState)

    @Bean("mailState")
    fun mailState(
        botRepository: BotRepository,
        genderState: State
    ): State = MailState(botRepository, genderState)

    @Bean("genderState")
    fun genderState(
        botRepository: BotRepository,
        roomCategoryState: State
    ): State = GenderState(botRepository, roomCategoryState)

    @Bean("roomCategoryState")
    fun roomCategoryState(
        botRepository: BotRepository,
        roomCategoryProperties: RoomCategoryProperties,
        danceStyleState: State
    ): State = RoomCategoryState(botRepository, roomCategoryProperties, danceStyleState)

    @Bean("danceStyleState")
    fun danceStyleState(
        botRepository: BotRepository,
        danceStyleProperties: DanceStyleProperties,
        neighborsState: State
    ): State = DanceStyleState(botRepository, danceStyleProperties, neighborsState)

    @Bean("neighborsState")
    fun neighborsState(
        botRepository: BotRepository,
        draftState: State
    ): State = NeighborsState(botRepository, draftState)

    @Bean("draftState")
    fun draftState(
        botRepository: BotRepository,
        removeDraftComponent: RemoveDraftComponent,
        exportState: State
    ): State = DraftReadyState(botRepository, removeDraftComponent, exportState)

    @Bean("exportState")
    fun exportRequestState(
        botRepository: BotRepository,
        googleSheets: GoogleSheetsService
    ): State = ExportRequestState(botRepository, googleSheets)
}
