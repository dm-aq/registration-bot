package ru.registration.bot.engine.commands.flow

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.MessageService
import ru.registration.bot.configuration.Category
import ru.registration.bot.configuration.RoomCategoryProperties
import ru.registration.bot.engine.commands.flow.StateType.ROOM_STATE
import ru.registration.bot.engine.commands.flow.states.RoomCategoryState
import ru.registration.bot.repositories.BotRepository
import ru.registration.bot.repositories.specifications.SetUserStatus

class RoomCategoryStateAskTest {
    @Test
    fun `asking for room category`() {
        // arrange
        val userId = 213
        val chatId = 1L
        val absSender: AbsSender = mock()
        val repo: BotRepository = mock()
        val nextState: State = mock()
        val messages: MessageService = mock {
            on { getMessage(any()) } doReturn "some-message"
        }
        val roomProperties = RoomCategoryProperties(
            mapOf(1 to Category("100", "test category"))
        )
        val roomCategoryState = RoomCategoryState(messages, repo, roomProperties, nextState)

        // act
        roomCategoryState.ask(userId, chatId, absSender)

        // assert
        val statusCaptor = argumentCaptor<SetUserStatus>()
        verify(repo).execute(statusCaptor.capture())
        assertEquals(
            SetUserStatus(userId, ROOM_STATE).sqlParameterSource,
            statusCaptor.firstValue.sqlParameterSource
        )

        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender).execute(messageCaptor.capture())
        assertEquals(chatId, messageCaptor.firstValue.chatId.toLong())
        assertTrue(messageCaptor.firstValue.text.startsWith("Выберите тип размещения:"))
    }
}
