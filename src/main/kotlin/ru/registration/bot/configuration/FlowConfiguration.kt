package ru.registration.bot.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.registration.bot.MessageService
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
        messages: MessageService,
        botRepository: BotRepository,
        fullNameState: State
    ): State = PhoneNumberState(messages, botRepository, fullNameState)

    @Bean("fullNameState")
    fun fullNameState(
        messages: MessageService,
        botRepository: BotRepository,
        mailState: State
    ): State = FullNameState(messages, botRepository, mailState)

    @Bean("mailState")
    fun mailState(
        messages: MessageService,
        botRepository: BotRepository,
        genderState: State
    ): State = MailState(messages, botRepository, genderState)

    @Bean("genderState")
    fun genderState(
        messages: MessageService,
        botRepository: BotRepository,
        roomCategoryState: State
    ): State = GenderState(messages, botRepository, roomCategoryState)

    @Bean("roomCategoryState")
    fun roomCategoryState(
        messages: MessageService,
        botRepository: BotRepository,
        roomCategoryProperties: RoomCategoryProperties,
        danceStyleState: State
    ): State = RoomCategoryState(messages, botRepository, roomCategoryProperties, danceStyleState)

    @Bean("danceStyleState")
    fun danceStyleState(
        messages: MessageService,
        botRepository: BotRepository,
        danceStyleProperties: DanceStyleProperties,
        neighborsState: State
    ): State = DanceStyleState(messages, botRepository, danceStyleProperties, neighborsState)

    @Bean("neighborsState")
    fun neighborsState(
        messages: MessageService,
        botRepository: BotRepository,
        draftState: State
    ): State = NeighborsState(messages, botRepository, draftState)

    @Bean("draftState")
    fun draftState(
        messages: MessageService,
        botRepository: BotRepository,
        removeDraftComponent: RemoveDraftComponent,
        exportState: State
    ): State = DraftReadyState(messages, botRepository, removeDraftComponent, exportState)

    @Bean("exportState")
    fun exportRequestState(
        messages: MessageService,
        botRepository: BotRepository,
        googleSheets: GoogleSheetsService
    ): State = ExportRequestState(messages, botRepository, googleSheets)
}
