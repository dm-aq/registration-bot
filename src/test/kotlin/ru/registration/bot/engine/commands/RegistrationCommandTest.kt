package ru.registration.bot.engine.commands

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Answers.RETURNS_DEEP_STUBS
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.engine.commands.flow.State

class RegistrationCommandTest {

    lateinit var commonFactory: CommonFactory
    lateinit var registrationCommand: RegistrationCommand

    @BeforeEach
    fun setUp() {
        commonFactory = mock()
        registrationCommand = RegistrationCommand(false, commonFactory)
    }

    @Test
    fun `new customer coming`() {
        // arrange
        val absSender: AbsSender = mock(defaultAnswer = RETURNS_DEEP_STUBS)

        val userId = 123
        val user: User = mock{
            on { id } doReturn 123
        }
        val chatId = 1L
        val chat: Chat = mock{
            on { id } doReturn chatId
        }
        val message: Message = mock {
            on { this.from } doReturn user
            on { this.chat } doReturn chat
        }
        val startState: State = mock()
        given { commonFactory.create(any(), any(), any(), anyOrNull()) }.willReturn(startState)

        // act
        registrationCommand.processMessage(absSender, message, null)

        // assert
        verify(startState).ask(eq(userId), eq(chatId), any())
    }

    @Test
    fun `customer has draft`() {
        TODO("Not yet implemented")
    }

    @Test
    fun `customer has finisher request`() {
        TODO("Not yet implemented")
    }

    @Test
    fun `registration is closed`() {
        TODO("Not yet implemented")
    }

    @Test
    fun `absSender is absent`() {
        TODO("Not yet implemented")
    }

    @Test
    fun `message is absent`() {
        TODO("Not yet implemented")
    }
}