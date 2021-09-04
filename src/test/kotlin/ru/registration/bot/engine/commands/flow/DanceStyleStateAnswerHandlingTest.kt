package ru.registration.bot.engine.commands.flow

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Answers.RETURNS_DEEP_STUBS
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.configuration.DanceStyleProperties
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.engine.commands.flow.StateType.DANCESTYLE_STATE
import ru.registration.bot.engine.commands.flow.StateType.NEIGHBORS_STATE
import ru.registration.bot.engine.commands.flow.states.DanceStyleState
import ru.registration.bot.engine.text
import ru.registration.bot.repositories.specifications.SetUserStatus

class DanceStyleStateAnswerHandlingTest {
    @Test
    fun `handling valid dance style`() {
        // arrange
        val user: User = mock {
            on { id } doReturn 213
        }
        val chat: Chat = mock {
            on { id } doReturn 1
        }
        val absSender: AbsSender = mock()
        val commonFactory: CommonFactory = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { danceStyleProperties } doReturn DanceStyleProperties(listOf("test-style"))
        }
        val danceStyleState = DanceStyleState(chat, user, absSender, commonFactory)
        val update: Update = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { text } doReturn "test-style"
        }

        // act
        danceStyleState.handle(update)

        // assert
        verify(commonFactory.requestRepository).execute(any())

        val statusCaptor = argumentCaptor<SetUserStatus>()
        verify(commonFactory.stateRepo).execute(statusCaptor.capture())
        assertEquals(
            SetUserStatus(213, DANCESTYLE_STATE, NEIGHBORS_STATE).sqlParameterSource,
            statusCaptor.firstValue.sqlParameterSource
        )

        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender).execute(messageCaptor.capture())
        assertEquals(1, messageCaptor.firstValue.chatId.toInt())
        assertTrue(messageCaptor.firstValue.text.startsWith("С кем будете жить"))
    }

    @Test
    fun `handling invalid dance style`() {
        // arrange
        val user: User = mock {
            on { id } doReturn 213
        }
        val chat: Chat = mock {
            on { id } doReturn 1
        }
        val absSender: AbsSender = mock()
        val commonFactory: CommonFactory = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { danceStyleProperties } doReturn DanceStyleProperties(listOf("test-style"))
        }
        val danceStyleState = DanceStyleState(chat, user, absSender, commonFactory)
        val update: Update = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { text } doReturn "invalid-dance-style"
        }

        // act
        danceStyleState.handle(update)

        // assert
        verifyZeroInteractions(commonFactory.requestRepository)
        verifyZeroInteractions(commonFactory.stateRepo)

        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender).execute(messageCaptor.capture())
        assertEquals(1, messageCaptor.firstValue.chatId.toInt())
        assertEquals("Неверное значение. Попробуйте еще раз.", messageCaptor.firstValue.text)
    }
}
