package ru.registration.bot.engine.commands.flow.states

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.engine.chat
import ru.registration.bot.engine.commands.RemoveDraftCommand
import ru.registration.bot.engine.commands.flow.State
import ru.registration.bot.engine.commands.flow.StateType.REQUEST_READY
import ru.registration.bot.engine.text
import ru.registration.bot.engine.user
import ru.registration.bot.repositories.specifications.SetUserStatus
import ru.registration.bot.repositories.specifications.UserRequest

class DraftReadyState(
    private val absSender: AbsSender?,
    private val commonFactory: CommonFactory,
    private val nextState: State
) : State {
    override fun ask(userId: Int, chatId: Long) {
        commonFactory.stateRepo.execute(SetUserStatus(userId, REQUEST_READY))
        val request = commonFactory.requestRepository.query(UserRequest(userId, REQUEST_READY)).first()

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
        absSender?.execute(SendMessage(chatId, message).setReplyMarkup(getInlineKeyboard()))
    }

    private fun getInlineKeyboard(): InlineKeyboardMarkup =
        InlineKeyboardMarkup()
            .setKeyboard(
                listOf(
                    listOf(InlineKeyboardButton().setText("Отправить").setCallbackData("отправить")),
                    listOf(InlineKeyboardButton().setText("Удалить черновик").setCallbackData("удалить"))
                )
            )

    override fun handle(update: Update) {

        when (update.text ?: "") {
            "отправить" ->
                nextState.handle(update)
            "удалить" ->
                RemoveDraftCommand(commonFactory)
                    .execute(absSender, update.user, update.chat, null) // todo refactor
        }
    }
}
