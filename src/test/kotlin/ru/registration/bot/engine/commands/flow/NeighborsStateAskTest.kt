package ru.registration.bot.engine.commands.flow

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Answers.RETURNS_DEEP_STUBS
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.configuration.DanceStyleProperties
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.engine.commands.flow.StateType.DANCESTYLE_STATE
import ru.registration.bot.engine.commands.flow.StateType.NEIGHBORS_STATE
import ru.registration.bot.engine.commands.flow.states.NeighborsState
import ru.registration.bot.repositories.specifications.SetUserStatus

class NeighborsStateAskTest {

    @Test
    fun `asking for neighbors`() {
        // arrange
        val user: User = mock {
            on { id } doReturn 213
        }
        val chat: Chat = mock {
            on { id } doReturn 1
        }
        val absSender: AbsSender = mock()
        val commonFactory: CommonFactory = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { danceStyleProperties } doReturn DanceStyleProperties(listOf("test-style"))
        }
        val neighborsState = NeighborsState(chat, user, absSender, commonFactory)

        // act
        neighborsState.ask()

        // assert

        val statusCaptor = argumentCaptor<SetUserStatus>()
        verify(commonFactory.stateRepo).execute(statusCaptor.capture())
        Assertions.assertEquals(
            SetUserStatus(213, DANCESTYLE_STATE, NEIGHBORS_STATE).sqlParameterSource,
            statusCaptor.firstValue.sqlParameterSource
        )

        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender).execute(messageCaptor.capture())
        Assertions.assertEquals(1, messageCaptor.firstValue.chatId.toInt())
        Assertions.assertTrue(messageCaptor.firstValue.text.startsWith("С кем будете жить"))
    }
}
