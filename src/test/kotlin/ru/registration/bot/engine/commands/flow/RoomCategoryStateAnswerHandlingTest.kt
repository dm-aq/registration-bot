package ru.registration.bot.engine.commands.flow

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Answers.RETURNS_DEEP_STUBS
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.configuration.Category
import ru.registration.bot.configuration.RoomCategoryProperties
import ru.registration.bot.engine.chatId
import ru.registration.bot.engine.commands.flow.states.RoomCategoryState
import ru.registration.bot.engine.text
import ru.registration.bot.engine.userId
import ru.registration.bot.repositories.RequestRepository
import ru.registration.bot.repositories.StateRepository

class RoomCategoryStateAnswerHandlingTest {

    @Test
    fun `handling valid room category`() {
        // arrange
        val userId = 213
        val chatId = 1L
        val absSender: AbsSender = mock()
        val stateRepo: StateRepository = mock()
        val requestRepo: RequestRepository = mock()
        val nextState: State = mock()
        val roomProperties = RoomCategoryProperties(
            mapOf(1 to Category("100", "test category"))
        )
        val roomCategoryState = RoomCategoryState(stateRepo, requestRepo, roomProperties, nextState)
        val update: Update = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { text } doReturn "1"
            on { this.userId } doReturn userId
            on { this.chatId } doReturn chatId
        }

        // act
        roomCategoryState.handle(update, absSender)

        // assert
        verify(requestRepo).execute(any())
        verify(nextState).ask(eq(userId), eq(chatId), any())
    }

    @Test
    fun `handling invalid room category`() {
        // arrange
        val userId = 213
        val chatId = 1L
        val absSender: AbsSender = mock()
        val stateRepo: StateRepository = mock()
        val requestRepo: RequestRepository = mock()
        val nextState: State = mock()
        val roomProperties = RoomCategoryProperties(emptyMap())
        val roomCategoryState = RoomCategoryState(stateRepo, requestRepo, roomProperties, nextState)
        val update: Update = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { text } doReturn "1"
            on { this.userId } doReturn userId
            on { this.chatId } doReturn chatId
        }

        // act
        roomCategoryState.handle(update, absSender)

        // assert
        verifyZeroInteractions(requestRepo)
        verifyZeroInteractions(nextState)

        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender).execute(messageCaptor.capture())
        assertEquals(chatId, messageCaptor.firstValue.chatId.toLong())
        assertEquals("Неверное значение. Попробуйте еще раз.", messageCaptor.firstValue.text)
    }
}
