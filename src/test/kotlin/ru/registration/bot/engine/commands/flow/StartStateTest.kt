package ru.registration.bot.engine.commands.flow

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.junit.jupiter.api.Test
import org.mockito.Answers
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.chatId
import ru.registration.bot.engine.commands.flow.states.StartState
import ru.registration.bot.engine.userId
import ru.registration.bot.repositories.RequestRepository

class StartStateTest {

    @Test
    fun `new registration`() {
        // arrange
        val userId = 213
        val chatId = 1L
        val absSender: AbsSender = mock()
        val requestRepo: RequestRepository = mock()
        val nextState: State = mock()
        val startState = StartState(requestRepo, nextState)
        val update: Update = mock(defaultAnswer = Answers.RETURNS_DEEP_STUBS) {
            on { hasCallbackQuery() } doReturn false
            on { message?.text } doReturn "new_registration"
            on { this.userId } doReturn userId
            on { this.chatId } doReturn chatId
        }

        // act
        startState.handle(update, absSender)

        // assert
        verify(requestRepo).execute(any())
        verify(nextState).ask(eq(userId), eq(chatId), any())
    }

    @Test
    fun `not new registration`() {
        // arrange
        val update: Update = mock(defaultAnswer = Answers.RETURNS_DEEP_STUBS) {
            on { hasCallbackQuery() } doReturn false
            on { message?.text } doReturn "another command"
        }
        val absSender: AbsSender = mock()
        val requestRepo: RequestRepository = mock()
        val nextState: State = mock()
        val startState = StartState(requestRepo, nextState)

        // act
        startState.handle(update, absSender)

        // assert
        verifyZeroInteractions(requestRepo)
        verifyZeroInteractions(absSender)
    }
}
