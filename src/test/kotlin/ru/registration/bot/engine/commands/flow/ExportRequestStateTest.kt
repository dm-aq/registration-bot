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
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.chatId
import ru.registration.bot.engine.commands.Request
import ru.registration.bot.engine.commands.flow.StateType.EXPORTED
import ru.registration.bot.engine.commands.flow.StateType.REQUEST_APPROVED
import ru.registration.bot.engine.commands.flow.states.ExportRequestState
import ru.registration.bot.engine.user
import ru.registration.bot.engine.userId
import ru.registration.bot.google.GoogleSheetsService
import ru.registration.bot.repositories.BotRepository
import ru.registration.bot.repositories.ExecSpecification
import ru.registration.bot.repositories.specifications.SetUserStatus
import ru.registration.bot.repositories.specifications.SetUserStatusByReqId
import ru.registration.bot.repositories.specifications.UpdateRequestField

class ExportRequestStateTest {

    @Test
    fun `export request`() {
        // arrange
        val userId = 213
        val chatId = 1L
        val userName = "some_tg_login"
        val absSender: AbsSender = mock()
        val request = Request(
            1, null, null, null, null, null, null, null, null, null
        )
        val repo: BotRepository = mock {
            on { query<Request>(any()) } doReturn listOf(request)
        }
        val googleSheets: GoogleSheetsService = mock()
        val exportRequestState = ExportRequestState(repo, googleSheets)
        val update: Update = mock(defaultAnswer = RETURNS_DEEP_STUBS) {
            on { this.userId } doReturn userId
            on { this.chatId } doReturn chatId
            on { this.user?.userName } doReturn userName
        }

        // act
        exportRequestState.handle(update, absSender)

        // assert

        val repoCaptor = argumentCaptor<ExecSpecification>()
        verify(repo, times(3)).execute(repoCaptor.capture())
        assertEquals(
            UpdateRequestField(update.userId, Pair("telegram_login", update.user?.userName)).sqlParameterSource,
            (repoCaptor.firstValue as UpdateRequestField).sqlParameterSource
        )
        assertEquals(
            SetUserStatus(userId, REQUEST_APPROVED).sqlParameterSource,
            (repoCaptor.secondValue as SetUserStatus).sqlParameterSource
        )
        assertEquals(
            SetUserStatusByReqId(1, EXPORTED).sqlParameterSource,
            (repoCaptor.thirdValue as SetUserStatusByReqId).sqlParameterSource
        )

        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender, times(2)).execute(messageCaptor.capture())

        assertEquals(1, messageCaptor.firstValue.chatId.toInt())
        assertTrue(messageCaptor.firstValue.text.startsWith("Круто, что вы едете с нами в этом году!"))

        assertEquals(1, messageCaptor.secondValue.chatId.toInt())
        assertTrue(messageCaptor.secondValue.text.startsWith("Обратите внимание"))

        verify(googleSheets).send(any())
    }

    @Test
    fun `asking for export`() {

        // arrange
        val userId = 213
        val chatId = 1L
        val absSender: AbsSender = mock()
        val requestRepo: BotRepository = mock()
        val googleSheets: GoogleSheetsService = mock()
        val exportRequestState = ExportRequestState(requestRepo, googleSheets)

        // act
        exportRequestState.ask(userId, chatId, absSender)

        // assert
        val messageCaptor = argumentCaptor<SendMessage>()
        verify(absSender).execute(messageCaptor.capture())
        assertEquals(chatId, messageCaptor.firstValue.chatId.toLong())
        assertEquals(messageCaptor.firstValue.text, "У вас уже есть заполненная заявка.")
    }
}
