package ru.registration.bot.engine.commands.flow.states

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.engine.commands.Emoji
import ru.registration.bot.engine.commands.flow.State
import ru.registration.bot.engine.commands.flow.StateType.EXPORTED
import ru.registration.bot.engine.commands.flow.StateType.REQUEST_APPROVED
import ru.registration.bot.engine.commands.flow.StateType.REQUEST_READY
import ru.registration.bot.repositories.specifications.SetUserStatus
import ru.registration.bot.repositories.specifications.SetUserStatusByReqId
import ru.registration.bot.repositories.specifications.UserRequest

class ExportRequestState(
    private val chat: Chat?,
    private val user: User?,
    private val absSender: AbsSender?,
    private val commonFactory: CommonFactory
) : State {

    fun export() {
        commonFactory.stateRepo.execute(SetUserStatus(user?.id, REQUEST_READY, REQUEST_APPROVED))

        absSender?.execute(SendMessage(chat?.id, """
            Круто, что вы едете с нами в этом году! 
            
            Обработка заявок производится вручную и может занимать до 2 суток. 
            Подтверждение придёт на указанную почту.
            
            Если по истечение 2-х суток вы не получите от нас письма, 
            свяжитесь с Надеждой ${Emoji.POINT_FINGER_RIGHT} @na_dy_mi ${Emoji.POINT_FINGER_LEFT}

        """.trimIndent()))

        absSender?.execute(SendMessage(chat?.id, """
            Обратите внимание, что на каждого человека необходимо заполнить отдельную форму регистрации.
            Если хотите зарегистрировать еще одного человека нажмите 
            ${Emoji.POINT_FINGER_RIGHT} /new_registration ${Emoji.POINT_FINGER_LEFT}
        """.trimIndent()))

        val request = commonFactory.requestRepository
            .query(UserRequest(user?.id, REQUEST_APPROVED))
            .first()

        commonFactory.googleSheets.send(request)

        commonFactory.stateRepo.execute(SetUserStatusByReqId(request.requestId, EXPORTED))
    }

    override fun ask(userId: Int, chatId: Long) {
        absSender?.execute(SendMessage(chatId, "У вас уже есть заполненная заявка."))
    }

    override fun handle(update: Update) {
        // there is nothing to do
    }
}
