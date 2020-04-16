package ru.registration.bot.engine.commands.flow

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.repositories.specifications.SetUserStatus
import ru.registration.bot.repositories.specifications.UserRequest

class DraftReadyState(
    private val chat: Chat?,
    private val user: User?,
    private val absSender: AbsSender?,
    private val commonFactory: CommonFactory
): State {
    override fun ask() {
        commonFactory.stateRepo.execute(SetUserStatus(user?.id, StateType.NEIGHBORS_STATE, StateType.REQUEST_READY))
        val request = commonFactory.requestRepository.query(UserRequest(user?.id, StateType.REQUEST_READY)).first()

        val message = """
            Проверьте пожалуйста данные заявки:
            
            Телефон: ${request.phone}
            Почта: ${request.email}
            Ф.И.О.: ${request.fullName}
            Пол: ${request.sex}
            Категория номера: ${request.roomType}
            Танцевальное направление: ${request.danceType}
            Соседи: ${request.neighbors}
        """.trimIndent()
        absSender?.execute(SendMessage(chat?.id, message).setReplyMarkup(getInlineKeyboard()))
    }

    private fun getInlineKeyboard(): InlineKeyboardMarkup{
        val row = listOf(
            InlineKeyboardButton().setText("Отправить").setCallbackData("отправить")
        )
        return InlineKeyboardMarkup().setKeyboard(listOf(row))
    }

    override fun handle(text: String?) {
        if (validate(text ?: "")) {
            ExportRequestState(chat, user, absSender, commonFactory).export()
        }
    }

    private fun validate(text: String): Boolean{
        return "отправить".equals(text, true)
    }
}