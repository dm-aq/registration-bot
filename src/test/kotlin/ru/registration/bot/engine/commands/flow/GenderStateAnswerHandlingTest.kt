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
import ru.registration.bot.MessageService
import ru.registration.bot.engine.chatId
import ru.registration.bot.engine.commands.flow.states.GenderState
import ru.registration.bot.engine.text
import ru.registration.bot.engine.userId
import ru.registration.bot.repositories.BotRepository

class GenderStateAnswerHandlingTest {
    @Test
    fun `handling valid gender`() {
        // arrange
        val userId = 213
        val chatId = 1L
        val absSender: AbsSender = mock()
        val repo: BotRepository = mock()
        val nextState: State = mock()
        val messages: MessageService = mock {
            on { getMessage(any()) } doReturn "some-message"
        }
        val sexState = GenderState(messages, repo, nextState)
        val update: Update = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { text } doReturn "F"
            on { this.userId } doReturn userId
            on { this.chatId } doReturn chatId
        }

        // act
        sexState.handle(update, absSender)

        // assert
        verify(repo).execute(any())
        verify(nextState).ask(eq(userId), eq(chatId), any())
    }

    @Test
    fun `handling invalid gender`() {
        // arrange
        val userId = 213
        val chatId = 1L
        val absSender: AbsSender = mock()
        val repo: BotRepository = mock()
        val nextState: State = mock()
        val messages: MessageService = mock {
            on { getMessage(any()) } doReturn "some-message"
        }
        val sexState = GenderState(messages, repo, nextState)
        val update: Update = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { text } doReturn "invalid_value"
            on { this.userId } doReturn userId
            on { this.chatId } doReturn chatId
        }

        // act
        sexState.handle(update, absSender)

        // assert
        verifyZeroInteractions(repo)
        verifyZeroInteractions(nextState)

        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender).execute(messageCaptor.capture())
        assertEquals(chatId, messageCaptor.firstValue.chatId.toLong())
        assertEquals("some-message", messageCaptor.firstValue.text)
    }
}
