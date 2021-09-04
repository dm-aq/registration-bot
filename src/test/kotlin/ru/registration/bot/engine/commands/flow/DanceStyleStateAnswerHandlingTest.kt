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
import ru.registration.bot.configuration.DanceStyleProperties
import ru.registration.bot.engine.commands.flow.states.DanceStyleState
import ru.registration.bot.engine.text
import ru.registration.bot.repositories.RequestRepository
import ru.registration.bot.repositories.StateRepository

class DanceStyleStateAnswerHandlingTest {
    @Test
    fun `handling valid dance style`() {
        // arrange
        val userId = 213
        val chatId = 1L
        val absSender: AbsSender = mock()
        val stateRepo: StateRepository = mock()
        val requestRepo: RequestRepository = mock()
        val nextState: State = mock()
        val danceStyleProperties = DanceStyleProperties(listOf("test-style"))
        val danceStyleState = DanceStyleState(stateRepo, requestRepo, danceStyleProperties, nextState)
        val update: Update = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { text } doReturn "test-style"
            on { userId } doReturn userId
            on { chatId } doReturn chatId
        }

        // act
        danceStyleState.handle(update, absSender)

        // assert
        verify(requestRepo).execute(any())
        verify(nextState.ask(eq(userId), eq(chatId), any()))
    }

    @Test
    fun `handling invalid dance style`() {
        // arrange
        val userId = 213
        val chatId = 1L
        val absSender: AbsSender = mock()
        val stateRepo: StateRepository = mock()
        val requestRepo: RequestRepository = mock()
        val nextState: State = mock()
        val danceStyleProperties = DanceStyleProperties(listOf("test-style"))
        val danceStyleState = DanceStyleState(stateRepo, requestRepo, danceStyleProperties, nextState)
        val update: Update = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { text } doReturn "invalid-dance-style"
            on { userId } doReturn userId
            on { chatId } doReturn chatId
        }

        // act
        danceStyleState.handle(update, absSender)

        // assert
        verifyZeroInteractions(requestRepo)
        verifyZeroInteractions(nextState)

        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender).execute(messageCaptor.capture())
        assertEquals(chatId, messageCaptor.firstValue.chatId.toInt())
        assertEquals("Неверное значение. Попробуйте еще раз.", messageCaptor.firstValue.text)
    }
}
