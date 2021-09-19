package ru.registration.bot.engine.commands

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.commands.flow.StateType.PHONE_STATE
import ru.registration.bot.repositories.BotRepository
import ru.registration.bot.repositories.specifications.SetUserStatus

class RemoveDraftComponentTest {

    @Test
    fun `removing draft`() {
        // arrange
        val stateRepo: BotRepository = mock()
        val absSender: AbsSender = mock()
        val removeDraftComponent = RemoveDraftComponent(stateRepo)
        val userId = 123
        val chatId = 1L

        // act
        removeDraftComponent.removeDraft(userId, chatId, absSender)

        // assert
        val statusCaptor = argumentCaptor<SetUserStatus>()
        verify(stateRepo).execute(statusCaptor.capture())
        assertEquals(
            SetUserStatus(userId, PHONE_STATE).sqlParameterSource,
            statusCaptor.firstValue.sqlParameterSource
        )

        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender, times(2)).execute(messageCaptor.capture())
        assertEquals(chatId, messageCaptor.firstValue.chatId.toLong())
        assertEquals("Черновик удален.", messageCaptor.firstValue.text)
        assertTrue(
            messageCaptor.secondValue.text
                .startsWith("Для того, чтобы заполнить заявку еще раз нажмите")
        )
    }
}
