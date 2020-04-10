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
import java.lang.NumberFormatException

class RoomCategoryState(
    private val chat: Chat?,
    private val user: User?,
    private val absSender: AbsSender?,
    private val commonFactory: CommonFactory
): State {
    override fun ask() {
        commonFactory.stateRepo.execute(SetUserStatus(user?.id, StateType.SEX_STATE, StateType.ROOM_STATE))
        absSender?.execute(
            SendMessage(chat?.id, "Выберите категорию номера:\n${getCategories()}")
                .setReplyMarkup(getInlineKeyboard())
        )
    }

    private fun getCategories() =
        commonFactory.roomCategoryProperties.categories
            .map { "${it.key}: ${it.value.description} (${it.value.price} руб/чел)" }.toList().joinToString("\n")

    private fun getInlineKeyboard(): InlineKeyboardMarkup {
        val row = commonFactory.roomCategoryProperties.categories.keys
            .map{InlineKeyboardButton().setText(it.toString()).setCallbackData(it.toString())}
            .toList()

        return InlineKeyboardMarkup().setKeyboard(listOf(row))
    }

    override fun handle(text: String?) {
        if (validate(text ?: "0")) {
            commonFactory.requestRepository.execute(
                UpdateRequestField(
                    user?.id,
                    Pair("room_type", (text?.toInt() ?: 0))
                )
            )
            DanceStyleState(chat, user, absSender, commonFactory).ask()
        }
    }

    private fun validate(text: String) =
        try {
            commonFactory.roomCategoryProperties.categories.containsKey(text.toInt())
                .also {
                    if (!it) {
                        sendErrorMessage()
                    }
                }
        }catch(exp: NumberFormatException){
            sendErrorMessage()
            false
        }

    private fun sendErrorMessage(){
        absSender?.execute(SendMessage(chat?.id, "Неверное значение. Попробуйте еще раз."))
    }
}
