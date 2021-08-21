package ru.registration.bot.engine.commands.flow

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Answers.RETURNS_DEEP_STUBS
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.engine.commands.Request
import ru.registration.bot.engine.commands.flow.StateType.EXPORTED
import ru.registration.bot.engine.commands.flow.StateType.REQUEST_APPROVED
import ru.registration.bot.engine.commands.flow.StateType.REQUEST_READY
import ru.registration.bot.repositories.ExecSpecification
import ru.registration.bot.repositories.specifications.SetUserStatus
import ru.registration.bot.repositories.specifications.SetUserStatusByReqId

class ExportRequestStateTest {

    @Test
    fun `export request`() {
        // arrange
        val user: User = mock{
            on { id } doReturn 213
        }
        val chat: Chat = mock{
            on { id } doReturn 1
        }
        val absSender: AbsSender = mock()
        val request = Request(
            1, null, null, null, null, null, null, null, null, null
        )
        val commonFactory: CommonFactory = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { requestRepository.query(any()) } doReturn listOf(request)
        }
        val exportRequestState = ExportRequestState(chat, user, absSender, commonFactory)

        // act
        exportRequestState.export()

        // assert

        val statusCaptor = argumentCaptor<ExecSpecification>()
        verify(commonFactory.stateRepo, times(2)).execute(statusCaptor.capture())
        assertEquals(
            SetUserStatus(213, REQUEST_READY, REQUEST_APPROVED).sqlParameterSource,
            (statusCaptor.firstValue as SetUserStatus).sqlParameterSource
        )
        assertEquals(
            SetUserStatusByReqId(1, EXPORTED).sqlParameterSource,
            (statusCaptor.secondValue as SetUserStatusByReqId).sqlParameterSource
        )

        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender, times(2)).execute(messageCaptor.capture())

        assertEquals(1, messageCaptor.firstValue.chatId.toInt())
        assertTrue(messageCaptor.firstValue.text.startsWith("Круто, что вы едете с нами в этом году!"))

        assertEquals(1, messageCaptor.secondValue.chatId.toInt())
        assertTrue(messageCaptor.secondValue.text.startsWith("Обратите внимание"))

        verify(commonFactory.googleSheets).send(any())
    }

    @Test
    fun `asking for export`() {

        // arrange
        val user: User = mock{
            on { id } doReturn 213
        }
        val chat: Chat = mock{
            on { id } doReturn 1
        }
        val absSender: AbsSender = mock()
        val commonFactory: CommonFactory = mock()
        val exportRequestState = ExportRequestState(chat, user, absSender, commonFactory)

        // act
        exportRequestState.ask()

        // assert
        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender).execute(messageCaptor.capture())
        assertEquals(1, messageCaptor.firstValue.chatId.toInt())
        assertEquals(messageCaptor.firstValue.text, "У вас уже есть заполненная заявка.")
    }
}
