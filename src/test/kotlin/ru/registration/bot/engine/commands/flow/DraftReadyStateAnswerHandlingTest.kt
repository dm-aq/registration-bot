package ru.registration.bot.engine.commands.flow

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.junit.jupiter.api.Test
import org.mockito.Answers.RETURNS_DEEP_STUBS
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.chatId
import ru.registration.bot.engine.commands.RemoveDraftComponent
import ru.registration.bot.engine.commands.Request
import ru.registration.bot.engine.commands.flow.states.DraftReadyState
import ru.registration.bot.engine.text
import ru.registration.bot.engine.userId
import ru.registration.bot.repositories.RequestRepository
import ru.registration.bot.repositories.StateRepository

class DraftReadyStateAnswerHandlingTest {

    @Test
    fun `handling send draft state`() {
        // arrange
        val userId = 213
        val chatId = 1L
        val absSender: AbsSender = mock()
        val stateRepo: StateRepository = mock()
        val request = Request(
            1, null, null, null, null, null, null, null, null, null
        )
        val requestRepo: RequestRepository = mock {
            on { query(any()) } doReturn listOf(request)
        }
        val removeDraftComponent: RemoveDraftComponent = mock()
        val nextState: State = mock()
        val draftReadyState = DraftReadyState(stateRepo, requestRepo, removeDraftComponent, nextState)
        val update: Update = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { text } doReturn "отправить"
            on { this.userId } doReturn userId
            on { this.chatId } doReturn chatId
        }

        // act
        draftReadyState.handle(update, absSender)

        // assert
        verify(nextState).handle(any(), any())
    }

    @Test
    fun `handling remove draft state`() {
        // arrange
        val userId = 213
        val chatId = 1L
        val absSender: AbsSender = mock()
        val stateRepo: StateRepository = mock()
        val request = Request(
            1, null, null, null, null, null, null, null, null, null
        )
        val requestRepo: RequestRepository = mock {
            on { query(any()) } doReturn listOf(request)
        }
        val removeDraftComponent: RemoveDraftComponent = mock()
        val nextState: State = mock()
        val draftReadyState = DraftReadyState(stateRepo, requestRepo, removeDraftComponent, nextState)
        val update: Update = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { text } doReturn "удалить"
            on { this.userId } doReturn userId
            on { this.chatId } doReturn chatId
        }

        // act
        draftReadyState.handle(update, absSender)

        // assert
        verifyZeroInteractions(requestRepo)
        verifyZeroInteractions(nextState)

        verify(removeDraftComponent).removeDraft(any(), any(), any())
    }
}
