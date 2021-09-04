package ru.registration.bot.engine.commands.flow

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Answers.RETURNS_DEEP_STUBS
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.engine.commands.flow.StateType.FULL_NAME_STATE
import ru.registration.bot.engine.commands.flow.StateType.PHONE_STATE
import ru.registration.bot.engine.commands.flow.states.PhoneNumberState
import ru.registration.bot.engine.text
import ru.registration.bot.repositories.specifications.SetUserStatus

class PhoneNumberStateAnswerHandlingTest {
    @Test
    fun `handling valid number`() {
        // arrange
        val user: User = mock {
            on { id } doReturn 213
        }
        val chat: Chat = mock {
            on { id } doReturn 1
        }
        val absSender: AbsSender = mock()
        val commonFactory: CommonFactory = mock(defaultAnswer = RETURNS_DEEP_STUBS)
        val phoneState = PhoneNumberState(chat, user, absSender, commonFactory)
        val update: Update = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { text } doReturn "89261234567"
        }

        // act
        phoneState.handle(update)

        // assert
        verify(commonFactory.requestRepository).execute(any())

        val statusCaptor = argumentCaptor<SetUserStatus>()
        verify(commonFactory.stateRepo).execute(statusCaptor.capture())
        assertEquals(
            SetUserStatus(213, PHONE_STATE, FULL_NAME_STATE).sqlParameterSource,
            statusCaptor.firstValue.sqlParameterSource
        )

        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender).execute(messageCaptor.capture())
        assertEquals(1, messageCaptor.firstValue.chatId.toInt())
        assertEquals("Как вас зовут (фио):", messageCaptor.firstValue.text)
    }

    @Test
    fun `handling invalid number`() {
        // arrange
        val user: User = mock {
            on { id } doReturn 213
        }
        val chat: Chat = mock {
            on { id } doReturn 1
        }
        val absSender: AbsSender = mock()
        val commonFactory: CommonFactory = mock(defaultAnswer = RETURNS_DEEP_STUBS)
        val phoneState = PhoneNumberState(chat, user, absSender, commonFactory)
        val update: Update = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { text } doReturn "invalid-number"
        }

        // act
        phoneState.handle(update)

        // assert
        verifyZeroInteractions(commonFactory.requestRepository)
        verifyZeroInteractions(commonFactory.stateRepo)

        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender).execute(messageCaptor.capture())
        assertEquals(1, messageCaptor.firstValue.chatId.toInt())
        assertEquals("Номер телефона должен быть в следующем формате: 8XXXYYYZZZZ", messageCaptor.firstValue.text)
    }

    @Test
    fun `handling empty number`() {
        // arrange
        val user: User = mock {
            on { id } doReturn 213
        }
        val chat: Chat = mock {
            on { id } doReturn 1
        }
        val absSender: AbsSender = mock()
        val commonFactory: CommonFactory = mock(defaultAnswer = RETURNS_DEEP_STUBS)
        val phoneState = PhoneNumberState(chat, user, absSender, commonFactory)
        val update: Update = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { text } doReturn null
        }

        // act
        phoneState.handle(update)

        // assert
        verifyZeroInteractions(commonFactory.requestRepository)
        verifyZeroInteractions(commonFactory.stateRepo)

        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender).execute(messageCaptor.capture())
        assertEquals(1, messageCaptor.firstValue.chatId.toInt())
        assertEquals("Номер телефона должен быть в следующем формате: 8XXXYYYZZZZ", messageCaptor.firstValue.text)
    }
}
