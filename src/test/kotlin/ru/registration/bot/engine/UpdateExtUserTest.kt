package ru.registration.bot.engine

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Answers.RETURNS_DEEP_STUBS
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User

class UpdateExtUserTest {

    lateinit var user: User

    @BeforeEach
    fun setUp() {
        user = mock{
            on { id } doReturn 123
        }
    }

    @Test
    fun `getting callback user`() {
        // arrange
        val update: Update = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { hasCallbackQuery() } doReturn true
            on { callbackQuery?.from } doReturn user
        }

        // act & assert
        assertEquals(user.id, update.user?.id)
    }

    @Test
    fun `getting message user`() {
        // arrange
        val update: Update = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { hasCallbackQuery() } doReturn false
            on { message?.from } doReturn user
        }

        // act & assert
        assertEquals(user.id, update.user?.id)
    }

    @Test
    fun `empty callback and message user value`() {
        // arrange
        val update: Update = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { hasCallbackQuery() } doReturn false
            on { message?.from } doReturn null
        }

        // act & assert
        assertNull(update.user?.id)
    }
}
