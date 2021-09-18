package ru.registration.bot.engine.commands.flow.states

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.configuration.RoomCategoryProperties
import ru.registration.bot.engine.chatId
import ru.registration.bot.engine.commands.flow.State
import ru.registration.bot.engine.commands.flow.StateType.ROOM_STATE
import ru.registration.bot.engine.text
import ru.registration.bot.engine.userId
import ru.registration.bot.repositories.BotRepository
import ru.registration.bot.repositories.specifications.SetUserStatus
import ru.registration.bot.repositories.specifications.UpdateRequestField

class RoomCategoryState(
    private val botRepository: BotRepository,
    private val roomCategoryProperties: RoomCategoryProperties,
    private val nextState: State
) : State {
    override fun ask(userId: Int, chatId: Long, absSender: AbsSender) {
        botRepository.execute(SetUserStatus(userId, ROOM_STATE))
        absSender.execute(
            SendMessage(chatId, "Выберите тип размещения:\n${getCategories()}")
                .setReplyMarkup(getInlineKeyboard())
        )
    }

    private fun getCategories() =
        roomCategoryProperties.categories
            .map { "${it.key}: ${it.value.description} (${it.value.price} руб/чел)" }.toList().joinToString("\n")

    private fun getInlineKeyboard(): InlineKeyboardMarkup {
        val row = roomCategoryProperties.categories.keys
            .map { InlineKeyboardButton().setText(it.toString()).setCallbackData(it.toString()) }
            .toList()

        return InlineKeyboardMarkup().setKeyboard(listOf(row))
    }

    override fun handle(update: Update, absSender: AbsSender) {
        try {
            if (update.text.isValid()) {
                botRepository.execute(
                    UpdateRequestField(
                        update.userId,
                        Pair("room_type", (update.text?.toInt() ?: 0))
                    )
                )
                nextState.ask(update.userId, update.chatId, absSender)
            } else {
                sendErrorMessage(update.chatId, absSender)
            }
        } catch (exp: NumberFormatException) {
            sendErrorMessage(update.chatId, absSender)
        }
    }

    private fun sendErrorMessage(chatId: Long, absSender: AbsSender) {
        absSender.execute(SendMessage(chatId, "Неверное значение. Попробуйте еще раз."))
    }

    private fun String?.isValid(): Boolean =
        roomCategoryProperties.categories.containsKey(this?.toInt() ?: 0)
}
