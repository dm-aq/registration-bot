package ru.registration.bot.engine.commands.flow

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Answers
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.repositories.specifications.SetUserStatus

class StartStateTest {

    @Test
    fun `new registration`() {
        // arrange
        val user: User = mock{
            on { id } doReturn 213
        }
        val chat: Chat = mock{
            on { id } doReturn 1
        }
        val absSender: AbsSender = mock()
        val commonFactory: CommonFactory = mock(defaultAnswer = Answers.RETURNS_DEEP_STUBS)
        val startState = StartState(chat, user, absSender, commonFactory)

        // act
        startState.ask()

        // assert
        verify(commonFactory.requestRepository).execute(any())

        val statusCaptor = argumentCaptor<SetUserStatus>()
        verify(commonFactory.stateRepo).execute(statusCaptor.capture())
        assertEquals(
            SetUserStatus(213, StateType.START_STATE, StateType.PHONE_STATE).sqlParameterSource,
            statusCaptor.firstValue.sqlParameterSource
        )

        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender).execute(messageCaptor.capture())
        assertEquals(1, messageCaptor.firstValue.chatId.toInt())
        assertEquals("Введите ваш номер телефона:", messageCaptor.firstValue.text)


    }
}
