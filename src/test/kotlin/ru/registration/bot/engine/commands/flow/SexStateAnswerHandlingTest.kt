package ru.registration.bot.engine.commands.flow

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Answers.RETURNS_DEEP_STUBS
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.commands.flow.states.SexState
import ru.registration.bot.engine.text
import ru.registration.bot.repositories.RequestRepository
import ru.registration.bot.repositories.StateRepository

class SexStateAnswerHandlingTest {
    @Test
    fun `handling valid gender`() {
        // arrange
        val userId = 213
        val chatId = 1L
        val absSender: AbsSender = mock()
        val stateRepo: StateRepository = mock()
        val requestRepo: RequestRepository = mock()
        val nextState: State = mock()
        val sexState = SexState(stateRepo, requestRepo, nextState)
        val update: Update = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { text } doReturn "F"
            on { userId } doReturn userId
            on { chatId } doReturn chatId
        }

        // act
        sexState.handle(update, absSender)

        // assert
        verify(requestRepo).execute(any())
        verify(nextState.ask(eq(userId), eq(chatId), any()))
    }

    @Test
    fun `handling invalid gender`() {
        // arrange
        val userId = 213
        val chatId = 1L
        val absSender: AbsSender = mock()
        val stateRepo: StateRepository = mock()
        val requestRepo: RequestRepository = mock()
        val nextState: State = mock()
        val sexState = SexState(stateRepo, requestRepo, nextState)
        val update: Update = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { text } doReturn "invalid_value"
            on { userId } doReturn userId
            on { chatId } doReturn chatId
        }

        // act
        sexState.handle(update, absSender)

        // assert
        verifyZeroInteractions(requestRepo)
        verifyZeroInteractions(nextState)

        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender).execute(messageCaptor.capture())
        assertEquals(chatId, messageCaptor.firstValue.chatId.toInt())
        assertEquals("Попробуйте еще раз.", messageCaptor.firstValue.text)
    }
}
