package ru.registration.bot.engine.commands

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Answers.RETURNS_DEEP_STUBS
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.MessageService

class StartCommandTest {
    @Test
    fun `command execution`() {
        // arrange
        val messages: MessageService = mock()
        val startCommand = StartCommand(messages)
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

        // act
        startCommand.processMessage(absSender, message, null)

        // assert
        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender).execute(messageCaptor.capture())
        assertEquals(chatId, messageCaptor.firstValue.chatId.toLong())
    }
}
