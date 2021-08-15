package ru.registration.bot.engine

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.mockito.Answers
import org.telegram.telegrambots.meta.api.objects.Update

class UpdateExtTextTest {

    @Test
    fun `getting callback text`() {
        // arrange
        val text = "some text"
        val update: Update = mock(defaultAnswer = Answers.RETURNS_DEEP_STUBS) {
            on { hasCallbackQuery() } doReturn true
            on { callbackQuery?.data } doReturn text
        }

        // act & assert
        assertEquals(text, update.text)
    }

    @Test
    fun `getting message text`() {
        // arrange
        val text = "some text"
        val update: Update = mock(defaultAnswer = Answers.RETURNS_DEEP_STUBS) {
            on { hasCallbackQuery() } doReturn false
            on { message?.text } doReturn text
        }

        // act & assert
        assertEquals(text, update.text)
    }

    @Test
    fun `empty callback and message text`() {
        // arrange
        val text = "some text"
        val update: Update = mock(defaultAnswer = Answers.RETURNS_DEEP_STUBS) {
            on { hasCallbackQuery() } doReturn false
            on { message?.text } doReturn null
        }

        // act & assert
        assertNull(update.text)
    }
}
