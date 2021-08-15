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
import ru.registration.bot.engine.commands.flow.StateType.PHONE_STATE
import ru.registration.bot.engine.commands.flow.StateType.START_STATE
import ru.registration.bot.repositories.specifications.SetUserStatus

class PhoneNumberStateAskTest{
    @Test
    fun `asking phone number`() {
        // arrange
        val user: User = mock{
            on { id } doReturn 213
        }
        val chat: Chat = mock{
            on { id } doReturn 1
        }
        val absSender: AbsSender = mock()
        val commonFactory: CommonFactory = mock(defaultAnswer = RETURNS_DEEP_STUBS)
        val phoneState = PhoneNumberState(chat, user, absSender, commonFactory)

        // act
        phoneState.ask()

        // assert
        val statusCaptor = argumentCaptor<SetUserStatus>()
        verify(commonFactory.stateRepo).execute(statusCaptor.capture())
        assertEquals(
            SetUserStatus(213, START_STATE, PHONE_STATE).sqlParameterSource,
            statusCaptor.firstValue.sqlParameterSource
        )

        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender).execute(messageCaptor.capture())
        assertEquals(1, messageCaptor.firstValue.chatId.toInt())
        assertEquals("Введите ваш номер телефона:", messageCaptor.firstValue.text)
    }
}
