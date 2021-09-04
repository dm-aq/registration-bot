package ru.registration.bot.engine.commands.flow

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Answers.RETURNS_DEEP_STUBS
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.engine.commands.flow.StateType.MAIL_STATE
import ru.registration.bot.engine.commands.flow.StateType.SEX_STATE
import ru.registration.bot.engine.commands.flow.states.SexState
import ru.registration.bot.repositories.specifications.SetUserStatus

class SexStateAskTest {
    @Test
    fun `asking for gender`() {
        // arrange
        val user: User = mock {
            on { id } doReturn 213
        }
        val chat: Chat = mock {
            on { id } doReturn 1
        }
        val absSender: AbsSender = mock()
        val commonFactory: CommonFactory = mock(defaultAnswer = RETURNS_DEEP_STUBS)
        val sexState = SexState(chat, user, absSender, commonFactory)

        // act
        sexState.ask()

        // assert
        val statusCaptor = argumentCaptor<SetUserStatus>()
        verify(commonFactory.stateRepo).execute(statusCaptor.capture())
        assertEquals(
            SetUserStatus(213, MAIL_STATE, SEX_STATE).sqlParameterSource,
            statusCaptor.firstValue.sqlParameterSource
        )

        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender).execute(messageCaptor.capture())
        assertEquals(1, messageCaptor.firstValue.chatId.toInt())
        assertEquals("Пол:", messageCaptor.firstValue.text)
    }
}
