package ru.registration.bot.engine.commands.flow.states

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.MessageService
import ru.registration.bot.engine.chatId
import ru.registration.bot.engine.commands.Emoji
import ru.registration.bot.engine.commands.flow.State
import ru.registration.bot.engine.commands.flow.StateType.EXPORTED
import ru.registration.bot.engine.commands.flow.StateType.REQUEST_APPROVED
import ru.registration.bot.engine.user
import ru.registration.bot.engine.userId
import ru.registration.bot.google.GoogleSheetsService
import ru.registration.bot.repositories.BotRepository
import ru.registration.bot.repositories.specifications.SetUserStatus
import ru.registration.bot.repositories.specifications.SetUserStatusByReqId
import ru.registration.bot.repositories.specifications.UpdateRequestField
import ru.registration.bot.repositories.specifications.UserRequest

class ExportRequestState(
    private val messages: MessageService,
    private val botRepository: BotRepository,
    private val googleSheets: GoogleSheetsService
) : State {

    override fun ask(userId: Int, chatId: Long, absSender: AbsSender) {
        absSender.execute(
            SendMessage(chatId, messages.getMessage("export_state_ask"))
        )
    }

    override fun handle(update: Update, absSender: AbsSender) {
        botRepository.execute(UpdateRequestField(update.userId, Pair("telegram_login", update.user?.userName)))
        botRepository.execute(SetUserStatus(update.userId, REQUEST_APPROVED))

        absSender.execute(
            SendMessage(update.chatId, messages.getMessage("export_state_congrats"))
        )

        absSender.execute(SendMessage(update.chatId, """
            ${messages.getMessage("export_state_notification")}  
            ${Emoji.POINT_FINGER_RIGHT} /new_registration ${Emoji.POINT_FINGER_LEFT}
        """.trimIndent()))

        val request = botRepository
            .query(UserRequest(update.userId, REQUEST_APPROVED))
            .first()

        googleSheets.send(request)

        botRepository.execute(SetUserStatusByReqId(request.requestId, EXPORTED))
    }
}
