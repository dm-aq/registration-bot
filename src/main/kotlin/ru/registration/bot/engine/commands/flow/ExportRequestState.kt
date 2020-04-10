package ru.registration.bot.engine.commands.flow

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.CommonFactory
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
        commonFactory.stateRepo.execute(SetUserStatus(user?.id, StateType.NEIGHBORS_STATE, StateType.REQUEST_READY))

        absSender?.execute(SendMessage(chat?.id, """
            Спасибо! Заявка заполнена.
            Мы свяжемся с вами в течении 24 часов.
        """.trimIndent()))

        val request = commonFactory.requestRepository.query(UserRequest(user?.id, StateType.REQUEST_READY)).first()

        commonFactory.googleSheets.send(request)

        commonFactory.stateRepo.execute(SetUserStatusByReqId(request.requestId, StateType.EXPORTED))
    }

    override fun ask() {
        absSender?.execute(SendMessage(chat?.id, "У вас уже есть заполненная заявка."))
    }

    override fun handle(text: String?) {}
}
