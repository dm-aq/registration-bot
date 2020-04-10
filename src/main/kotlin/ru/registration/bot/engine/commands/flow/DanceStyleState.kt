package ru.registration.bot.engine.commands.flow

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.repositories.specifications.SetUserStatus
import ru.registration.bot.repositories.specifications.UpdateRequestField

class DanceStyleState(
    private val chat: Chat?,
    private val user: User?,
    private val absSender: AbsSender?,
    private val commonFactory: CommonFactory
): State {
    override fun ask() {
        commonFactory.stateRepo.execute(SetUserStatus(user?.id, StateType.DANCESTYLE_STATE))
        absSender?.execute(
            SendMessage(chat?.id, "Выберите танцевальное направление:")
                .setReplyMarkup(getInlineKeyboard())
        )
    }

    private fun getInlineKeyboard(): InlineKeyboardMarkup{
        val row = commonFactory.danceStyleProperties.values
            .map { InlineKeyboardButton().setText(it).setCallbackData(it) }
            .toList()

        return InlineKeyboardMarkup().setKeyboard(listOf(row))
    }

    override fun handle(text: String?) {
        if (validate(text ?: "")) {
            commonFactory.requestRepository.execute(UpdateRequestField(user?.id, Pair("dance_type", text ?: "")))
            NeighborsState(chat, user, absSender, commonFactory).ask()
        }
    }

    private fun validate(text: String) =
        commonFactory.danceStyleProperties.values.contains(text.toLowerCase())
            .also {
                if (!it){
                    absSender?.execute(SendMessage(chat?.id, "Неверное значение. Попробуйте еще раз."))
                }
            }
}
