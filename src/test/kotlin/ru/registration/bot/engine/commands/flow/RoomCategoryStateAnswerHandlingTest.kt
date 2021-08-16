package ru.registration.bot.engine.commands.flow

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Answers.RETURNS_DEEP_STUBS
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.configuration.Category
import ru.registration.bot.configuration.DanceStyleProperties
import ru.registration.bot.configuration.RoomCategoryProperties
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.engine.commands.flow.StateType.DANCESTYLE_STATE
import ru.registration.bot.engine.commands.flow.StateType.ROOM_STATE
import ru.registration.bot.engine.text
import ru.registration.bot.repositories.specifications.SetUserStatus

class RoomCategoryStateAnswerHandlingTest{

    @Test
    fun `handling valid room category`() {
        // arrange
        val user: User = mock{
            on { id } doReturn 213
        }
        val chat: Chat = mock{
            on { id } doReturn 1
        }
        val absSender: AbsSender = mock()
        val commonFactory: CommonFactory = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { roomCategoryProperties } doReturn RoomCategoryProperties(
                mapOf(0 to Category("100", "test category"))
            )
            on { danceStyleProperties } doReturn DanceStyleProperties(listOf("test-style"))
        }
        val roomCategoryState = RoomCategoryState(chat, user, absSender, commonFactory)
        val update: Update = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { text } doReturn "0"
        }

        // act
        roomCategoryState.handle(update)

        // assert
        verify(commonFactory.requestRepository).execute(any())

        val statusCaptor = argumentCaptor<SetUserStatus>()
        verify(commonFactory.stateRepo).execute(statusCaptor.capture())
        assertEquals(
            SetUserStatus(213, ROOM_STATE, DANCESTYLE_STATE).sqlParameterSource,
            statusCaptor.firstValue.sqlParameterSource
        )

        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender).execute(messageCaptor.capture())
        assertEquals(1, messageCaptor.firstValue.chatId.toInt())
        assertTrue(messageCaptor.firstValue.text.startsWith("Выберите танцевальное направление:"))
    }

    @Test
    fun `handling invalid room category`() {
        // arrange
        val user: User = mock{
            on { id } doReturn 213
        }
        val chat: Chat = mock{
            on { id } doReturn 1
        }
        val absSender: AbsSender = mock()
        val commonFactory: CommonFactory = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { roomCategoryProperties } doReturn RoomCategoryProperties(emptyMap())
        }
        val roomCategoryState = RoomCategoryState(chat, user, absSender, commonFactory)
        val update: Update = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { text } doReturn "1"
        }

        // act
        roomCategoryState.handle(update)

        // assert
        verifyZeroInteractions(commonFactory.requestRepository)
        verifyZeroInteractions(commonFactory.stateRepo)

        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender).execute(messageCaptor.capture())
        assertEquals(1, messageCaptor.firstValue.chatId.toInt())
        assertEquals("Неверное значение. Попробуйте еще раз.", messageCaptor.firstValue.text)
    }
}