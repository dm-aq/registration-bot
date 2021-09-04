package ru.registration.bot.engine.commands.flow

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.commands.RemoveDraftComponent
import ru.registration.bot.engine.commands.Request
import ru.registration.bot.engine.commands.flow.StateType.REQUEST_READY
import ru.registration.bot.engine.commands.flow.states.DraftReadyState
import ru.registration.bot.repositories.RequestRepository
import ru.registration.bot.repositories.StateRepository
import ru.registration.bot.repositories.specifications.SetUserStatus

class DraftReadyStateAskTest {
    @Test
    fun `asking for draft check`() {
        // arrange
        val userId = 213
        val chatId = 1L
        val absSender: AbsSender = mock()
        val stateRepo: StateRepository = mock()
        val request = Request(
            null, null, null, null, null, null, null, null, null, null
        )
        val requestRepo: RequestRepository = mock {
            on { query(any()) } doReturn listOf(request)
        }
        val removeDraftComponent: RemoveDraftComponent = mock()
        val nextState: State = mock()
        val draftReadyState = DraftReadyState(stateRepo, requestRepo, removeDraftComponent, nextState)

        // act
        draftReadyState.ask(userId, chatId, absSender)

        // assert

        val statusCaptor = argumentCaptor<SetUserStatus>()
        verify(stateRepo).execute(statusCaptor.capture())
        Assertions.assertEquals(
            SetUserStatus(userId, REQUEST_READY).sqlParameterSource,
            statusCaptor.firstValue.sqlParameterSource
        )

        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender).execute(messageCaptor.capture())
        Assertions.assertEquals(chatId, messageCaptor.firstValue.chatId.toLong())
        Assertions.assertTrue(messageCaptor.firstValue.text.startsWith("Проверьте пожалуйста данные заявки:"))
    }
}
