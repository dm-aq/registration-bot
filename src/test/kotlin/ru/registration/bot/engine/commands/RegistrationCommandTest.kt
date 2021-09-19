package ru.registration.bot.engine.commands

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Answers.RETURNS_DEEP_STUBS
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.MessageService
import ru.registration.bot.engine.UserStateFactory
import ru.registration.bot.engine.commands.flow.State
import ru.registration.bot.engine.commands.flow.states.GenderState
import ru.registration.bot.repositories.BotRepository

class RegistrationCommandTest {

    @Test
    fun `new customer coming`() {
        // arrange
        val userStateFactory: UserStateFactory = mock()
        val messages: MessageService = mock {
            on { getMessage(any()) } doReturn "some-message"
        }
        val registrationCommand = RegistrationCommand(false, messages, userStateFactory)
        val absSender: AbsSender = mock(defaultAnswer = RETURNS_DEEP_STUBS)

        val userId = 123
        val user: User = mock {
            on { id } doReturn userId
        }
        val chatId = 1L
        val chat: Chat = mock {
            on { id } doReturn chatId
        }
        val message: Message = mock {
            on { this.from } doReturn user
            on { this.chat } doReturn chat
        }
        val startState: State = mock()
        given { userStateFactory.create(any()) }.willReturn(startState)

        // act
        registrationCommand.processMessage(absSender, message, null)

        // assert
        verify(startState).ask(eq(userId), eq(chatId), any())
    }

    @Test
    fun `customer has draft`() {
        // arrange
        val userStateFactory: UserStateFactory = mock()
        val messages: MessageService = mock {
            on { getMessage(any()) } doReturn "some-message"
        }
        val registrationCommand = RegistrationCommand(false, messages, userStateFactory)
        val absSender: AbsSender = mock(defaultAnswer = RETURNS_DEEP_STUBS)

        val userId = 123
        val user: User = mock {
            on { id } doReturn userId
        }
        val chatId = 1L
        val chat: Chat = mock {
            on { id } doReturn chatId
        }
        val message: Message = mock {
            on { this.from } doReturn user
            on { this.chat } doReturn chat
        }

        val repo: BotRepository = mock()
        val nextState: State = mock()
        val state = GenderState(messages, repo, nextState)
        given { userStateFactory.create(any()) }.willReturn(state)

        // act
        registrationCommand.processMessage(absSender, message, null)

        // assert
        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender, times(2)).execute(messageCaptor.capture())
        assertEquals(chatId, messageCaptor.firstValue.chatId.toLong())
        assertEquals("some-message", messageCaptor.firstValue.text)
    }

    @Test
    fun `registration is closed`() {
        // arrange
        val userStateFactory: UserStateFactory = mock()
        val messages: MessageService = mock {
            on { getMessage(any()) } doReturn "some-message"
        }
        val registrationCommand = RegistrationCommand(true, messages, userStateFactory)
        val absSender: AbsSender = mock(defaultAnswer = RETURNS_DEEP_STUBS)

        val user: User = mock {
            on { id } doReturn 123
        }
        val chatId = 1L
        val chat: Chat = mock {
            on { id } doReturn chatId
        }
        val message: Message = mock {
            on { this.from } doReturn user
            on { this.chat } doReturn chat
        }
        val startState: State = mock()
        given { userStateFactory.create(any()) }.willReturn(startState)

        // act
        registrationCommand.processMessage(absSender, message, null)

        // assert
        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender).execute(messageCaptor.capture())
        assertEquals(chatId, messageCaptor.firstValue.chatId.toLong())
        assertEquals("some-message", messageCaptor.firstValue.text)

        verifyZeroInteractions(startState)
    }

    @Test
    fun `absSender is absent`() {
        // arrange
        val userStateFactory: UserStateFactory = mock()
        val messages: MessageService = mock {
            on { getMessage(any()) } doReturn "some-message"
        }
        val registrationCommand = RegistrationCommand(false, messages, userStateFactory)
        val absSender: AbsSender? = null

        val user: User = mock {
            on { id } doReturn 123
        }
        val chatId = 1L
        val chat: Chat = mock {
            on { id } doReturn chatId
        }
        val message: Message = mock {
            on { this.from } doReturn user
            on { this.chat } doReturn chat
        }
        val startState: State = mock()
        given { userStateFactory.create(any()) }.willReturn(startState)

        // act
        registrationCommand.processMessage(absSender, message, null)

        // assert
        verifyZeroInteractions(startState)
    }

    @Test
    fun `message is absent`() {
        // arrange
        val userStateFactory: UserStateFactory = mock()
        val messages: MessageService = mock {
            on { getMessage(any()) } doReturn "some-message"
        }
        val registrationCommand = RegistrationCommand(false, messages, userStateFactory)
        val absSender: AbsSender = mock(defaultAnswer = RETURNS_DEEP_STUBS)

        val message: Message? = null
        val startState: State = mock()
        given { userStateFactory.create(any()) }.willReturn(startState)

        // act
        registrationCommand.processMessage(absSender, message, null)

        // assert
        verifyZeroInteractions(startState)
    }
}
