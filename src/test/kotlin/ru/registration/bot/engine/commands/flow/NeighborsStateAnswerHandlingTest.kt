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
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.engine.commands.Request
import ru.registration.bot.engine.commands.flow.StateType.NEIGHBORS_STATE
import ru.registration.bot.engine.commands.flow.StateType.REQUEST_READY
import ru.registration.bot.engine.commands.flow.states.NeighborsState
import ru.registration.bot.engine.text
import ru.registration.bot.repositories.specifications.SetUserStatus

class NeighborsStateAnswerHandlingTest {

    @Test
    fun `handling valid neighbors`() {
        // arrange
        val user: User = mock {
            on { id } doReturn 213
        }
        val chat: Chat = mock {
            on { id } doReturn 1
        }
        val absSender: AbsSender = mock()
        val request = Request(
            null, null, null, null, null, null, null, null, null, null
        )
        val commonFactory: CommonFactory = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { requestRepository.query(any()) } doReturn listOf(request)
        }
        val neighborsState = NeighborsState(chat, user, absSender, commonFactory)
        val update: Update = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { text } doReturn "Jonh Smith"
        }

        // act
        neighborsState.handle(update)

        // assert
        verify(commonFactory.requestRepository).execute(any())

        val statusCaptor = argumentCaptor<SetUserStatus>()
        verify(commonFactory.stateRepo).execute(statusCaptor.capture())
        assertEquals(
            SetUserStatus(213, NEIGHBORS_STATE, REQUEST_READY).sqlParameterSource,
            statusCaptor.firstValue.sqlParameterSource
        )

        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender).execute(messageCaptor.capture())
        assertEquals(1, messageCaptor.firstValue.chatId.toInt())
        assertTrue(messageCaptor.firstValue.text.startsWith("Проверьте пожалуйста данные заявки:"))
    }

    @Test
    fun `handling invalid neighbors`() {
        // arrange
        val user: User = mock {
            on { id } doReturn 213
        }
        val chat: Chat = mock {
            on { id } doReturn 1
        }
        val absSender: AbsSender = mock()
        val request = Request(
            null, null, null, null, null, null, null, null, null, null
        )
        val commonFactory: CommonFactory = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { requestRepository.query(any()) } doReturn listOf(request)
        }
        val neighborsState = NeighborsState(chat, user, absSender, commonFactory)
        val update: Update = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { text } doReturn "Jonh Smith".repeat(14)
        }

        // act
        neighborsState.handle(update)

        // assert
        verifyZeroInteractions(commonFactory.requestRepository)
        verifyZeroInteractions(commonFactory.stateRepo)

        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender).execute(messageCaptor.capture())
        assertEquals(1, messageCaptor.firstValue.chatId.toInt())
        assertEquals("Слишком много букв. Будьте скромнее.", messageCaptor.firstValue.text)
    }
}
