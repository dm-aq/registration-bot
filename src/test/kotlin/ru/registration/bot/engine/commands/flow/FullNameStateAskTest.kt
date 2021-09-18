package ru.registration.bot.engine.commands.flow

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.commands.flow.StateType.FULL_NAME_STATE
import ru.registration.bot.engine.commands.flow.states.FullNameState
import ru.registration.bot.repositories.BotRepository
import ru.registration.bot.repositories.specifications.SetUserStatus

class FullNameStateAskTest {
    @Test
    fun `asking for full name`() {
        // arrange
        val userId = 213
        val chatId = 1L
        val absSender: AbsSender = mock()
        val repo: BotRepository = mock()
        val nextState: State = mock()
        val fullNameState = FullNameState(repo, nextState)

        // act
        fullNameState.ask(userId, chatId, absSender)

        // assert
        val statusCaptor = argumentCaptor<SetUserStatus>()
        verify(repo).execute(statusCaptor.capture())
        assertEquals(
            SetUserStatus(userId, FULL_NAME_STATE).sqlParameterSource,
            statusCaptor.firstValue.sqlParameterSource
        )

        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender).execute(messageCaptor.capture())
        assertEquals(chatId, messageCaptor.firstValue.chatId.toLong())
        assertEquals("Как вас зовут (фио):", messageCaptor.firstValue.text)
    }
}
