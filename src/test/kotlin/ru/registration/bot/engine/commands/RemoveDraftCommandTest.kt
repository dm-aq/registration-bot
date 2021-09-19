package ru.registration.bot.engine.commands

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Answers.RETURNS_DEEP_STUBS
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.MessageService
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.engine.commands.flow.StateType

class RemoveDraftCommandTest {

    lateinit var commonFactory: CommonFactory
    lateinit var removeDraftComponent: RemoveDraftComponent
    lateinit var messages: MessageService
    lateinit var removeDraftCommand: RemoveDraftCommand

    @BeforeEach
    fun setUp() {
        commonFactory = mock()
        removeDraftComponent = mock()
        messages = mock()
        removeDraftCommand = RemoveDraftCommand(messages, commonFactory, removeDraftComponent)
    }

    @Test
    fun `customer has draft`() {
        // arrange
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

        given { commonFactory.currentUserStateType(any()) }.willReturn(StateType.GENDER_STATE)

        // act
        removeDraftCommand.processMessage(absSender, message, null)

        // assert
        verify(removeDraftComponent).removeDraft(any(), any(), any())
    }

    @Test
    fun `customer doesn't have draft`() {
        // arrange
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

        given { commonFactory.currentUserStateType(any()) }.willReturn(StateType.EXPORTED)
        given { messages.getMessage(any()) }.willReturn("some-message")

        // act
        removeDraftCommand.processMessage(absSender, message, null)

        // assert
        verifyZeroInteractions(removeDraftComponent)

        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender).execute(messageCaptor.capture())
        assertEquals(chatId, messageCaptor.firstValue.chatId.toLong())
        assertEquals("some-message", messageCaptor.firstValue.text)
    }
}
