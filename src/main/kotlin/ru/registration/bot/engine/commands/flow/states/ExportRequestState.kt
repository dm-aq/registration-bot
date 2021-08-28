package ru.registration.bot.engine.commands.flow.states

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.chatId
import ru.registration.bot.engine.commands.Emoji
import ru.registration.bot.engine.commands.flow.State
import ru.registration.bot.engine.commands.flow.StateType.EXPORTED
import ru.registration.bot.engine.commands.flow.StateType.REQUEST_APPROVED
import ru.registration.bot.engine.userId
import ru.registration.bot.google.GoogleSheetsService
import ru.registration.bot.repositories.RequestRepository
import ru.registration.bot.repositories.StateRepository
import ru.registration.bot.repositories.specifications.SetUserStatus
import ru.registration.bot.repositories.specifications.SetUserStatusByReqId
import ru.registration.bot.repositories.specifications.UserRequest

class ExportRequestState(
    private val stateRepo: StateRepository,
    private val requestRepository: RequestRepository,
    private val googleSheets: GoogleSheetsService
) : State {

    override fun ask(userId: Int, chatId: Long, absSender: AbsSender) {
        absSender.execute(SendMessage(chatId, "У вас уже есть заполненная заявка."))
    }

    override fun handle(update: Update, absSender: AbsSender) {
        stateRepo.execute(SetUserStatus(update.userId, REQUEST_APPROVED))

        absSender.execute(SendMessage(update.chatId, """
            Круто, что вы едете с нами в этом году! 
            
            Обработка заявок производится вручную и может занимать до 2 суток. 
            Подтверждение придёт на указанную почту.
            
            Если по истечение 2-х суток вы не получите от нас письма, 
            свяжитесь с Надеждой ${Emoji.POINT_FINGER_RIGHT} @na_dy_mi ${Emoji.POINT_FINGER_LEFT}

        """.trimIndent()))

        absSender.execute(SendMessage(update.chatId, """
            Обратите внимание, что на каждого человека необходимо заполнить отдельную форму регистрации.
            Если хотите зарегистрировать еще одного человека нажмите 
            ${Emoji.POINT_FINGER_RIGHT} /new_registration ${Emoji.POINT_FINGER_LEFT}
        """.trimIndent()))

        val request = requestRepository
            .query(UserRequest(update.userId, REQUEST_APPROVED))
            .first()

        googleSheets.send(request)

        stateRepo.execute(SetUserStatusByReqId(request.requestId, EXPORTED))
    }
}
