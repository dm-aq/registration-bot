package ru.registration.bot.engine.commands.flow

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.MessageService
import ru.registration.bot.configuration.DanceStyleProperties
import ru.registration.bot.engine.commands.flow.StateType.DANCESTYLE_STATE
import ru.registration.bot.engine.commands.flow.states.DanceStyleState
import ru.registration.bot.repositories.BotRepository
import ru.registration.bot.repositories.specifications.SetUserStatus

class DanceStyleStateAskTest {

    @Test
    fun `asking for dance style`() {
        // arrange
        val userId = 213
        val chatId = 1L
        val absSender: AbsSender = mock()
        val repo: BotRepository = mock()
        val nextState: State = mock()
        val danceStyleProperties = DanceStyleProperties(listOf("test-style"))
        val messages: MessageService = mock {
            on { getMessage(any()) } doReturn "some-message"
        }
        val danceStyleState = DanceStyleState(messages, repo, danceStyleProperties, nextState)

        // act
        danceStyleState.ask(userId, chatId, absSender)

        // assert

        val statusCaptor = argumentCaptor<SetUserStatus>()
        verify(repo).execute(statusCaptor.capture())
        assertEquals(
            SetUserStatus(userId, DANCESTYLE_STATE).sqlParameterSource,
            statusCaptor.firstValue.sqlParameterSource
        )

        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender).execute(messageCaptor.capture())
        assertEquals(chatId, messageCaptor.firstValue.chatId.toLong())
        assertTrue(messageCaptor.firstValue.text.startsWith("some-message"))
    }
}
