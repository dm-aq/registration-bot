package ru.registration.bot.engine.commands.flow

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.commands.flow.StateType.NEIGHBORS_STATE
import ru.registration.bot.engine.commands.flow.states.NeighborsState
import ru.registration.bot.repositories.RequestRepository
import ru.registration.bot.repositories.StateRepository
import ru.registration.bot.repositories.specifications.SetUserStatus

class NeighborsStateAskTest {

    @Test
    fun `asking for neighbors`() {
        // arrange
        val userId = 213
        val chatId = 1L
        val absSender: AbsSender = mock()
        val stateRepo: StateRepository = mock()
        val requestRepo: RequestRepository = mock()
        val nextState: State = mock()
        val neighborsState = NeighborsState(stateRepo, requestRepo, nextState)

        // act
        neighborsState.ask(userId, chatId, absSender)

        // assert

        val statusCaptor = argumentCaptor<SetUserStatus>()
        verify(stateRepo).execute(statusCaptor.capture())
        Assertions.assertEquals(
            SetUserStatus(userId, NEIGHBORS_STATE).sqlParameterSource,
            statusCaptor.firstValue.sqlParameterSource
        )

        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender).execute(messageCaptor.capture())
        Assertions.assertEquals(chatId, messageCaptor.firstValue.chatId.toLong())
        Assertions.assertTrue(messageCaptor.firstValue.text.startsWith("С кем будете жить"))
    }
}
