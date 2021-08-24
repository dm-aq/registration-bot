package ru.registration.bot.engine

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Answers.RETURNS_DEEP_STUBS
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Update

class UpdateExtChatTest {

    lateinit var chat: Chat

    @BeforeEach
    fun setUp() {
        chat = mock {
            on { id } doReturn 1L
        }
    }

    @Test
    fun `getting callback chat`() {
        // arrange
        val update: Update = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { hasCallbackQuery() } doReturn true
            on { callbackQuery?.message?.chat } doReturn chat
        }

        // act & assert
        assertEquals(chat.id, update.chat?.id)
    }

    @Test
    fun `getting message chat`() {
        // arrange
        val update: Update = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { hasCallbackQuery() } doReturn false
            on { message?.chat } doReturn chat
        }

        // act & assert
        assertEquals(chat.id, update.chat?.id)
    }

    @Test
    fun `empty callback and message chat value`() {
        // arrange
        val update: Update = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { hasCallbackQuery() } doReturn false
            on { message?.chat } doReturn null
        }

        // act & assert
        assertNull(update.chat?.id)
    }

    @Test
    fun `test chatId`() {
        TODO("Not yet implemented")
    }
}
