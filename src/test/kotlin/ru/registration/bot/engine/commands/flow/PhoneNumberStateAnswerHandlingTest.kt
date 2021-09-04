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
import ru.registration.bot.engine.commands.flow.states.PhoneNumberState
import ru.registration.bot.engine.text
import ru.registration.bot.repositories.RequestRepository
import ru.registration.bot.repositories.StateRepository

class PhoneNumberStateAnswerHandlingTest {
    @Test
    fun `handling valid number`() {
        // arrange
        val userId = 213
        val chatId = 1L
        val absSender: AbsSender = mock()
        val stateRepo: StateRepository = mock()
        val requestRepo: RequestRepository = mock()
        val nextState: State = mock()
        val phoneState = PhoneNumberState(stateRepo, requestRepo, nextState)
        val update: Update = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { text } doReturn "89261234567"
            on { userId } doReturn userId
            on { chatId } doReturn chatId
        }

        // act
        phoneState.handle(update, absSender)

        // assert
        verify(requestRepo).execute(any())
        verify(nextState.ask(eq(userId), eq(chatId), any()))
    }

    @Test
    fun `handling invalid number`() {
        // arrange
        val userId = 213
        val chatId = 1L
        val absSender: AbsSender = mock()
        val stateRepo: StateRepository = mock()
        val requestRepo: RequestRepository = mock()
        val nextState: State = mock()
        val phoneState = PhoneNumberState(stateRepo, requestRepo, nextState)
        val update: Update = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { text } doReturn "invalid-number"
            on { userId } doReturn userId
            on { chatId } doReturn chatId
        }

        // act
        phoneState.handle(update, absSender)

        // assert
        verifyZeroInteractions(requestRepo)
        verifyZeroInteractions(nextState)

        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender).execute(messageCaptor.capture())
        assertEquals(chatId, messageCaptor.firstValue.chatId.toInt())
        assertEquals("Номер телефона должен быть в следующем формате: 8XXXYYYZZZZ", messageCaptor.firstValue.text)
    }

    @Test
    fun `handling empty number`() {
        // arrange
        val userId = 213
        val chatId = 1L
        val absSender: AbsSender = mock()
        val stateRepo: StateRepository = mock()
        val requestRepo: RequestRepository = mock()
        val nextState: State = mock()
        val phoneState = PhoneNumberState(stateRepo, requestRepo, nextState)
        val update: Update = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { text } doReturn null
            on { userId } doReturn userId
            on { chatId } doReturn chatId
        }

        // act
        phoneState.handle(update, absSender)

        // assert
        verifyZeroInteractions(requestRepo)
        verifyZeroInteractions(nextState)

        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender).execute(messageCaptor.capture())
        assertEquals(chatId, messageCaptor.firstValue.chatId.toInt())
        assertEquals("Номер телефона должен быть в следующем формате: 8XXXYYYZZZZ", messageCaptor.firstValue.text)
    }
}
