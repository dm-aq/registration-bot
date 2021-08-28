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
import ru.registration.bot.repositories.RequestRepository
import ru.registration.bot.repositories.StateRepository
import ru.registration.bot.repositories.specifications.SetUserStatus
import ru.registration.bot.repositories.specifications.UpdateRequestField

class RoomCategoryState(
    private val absSender: AbsSender,
    private val stateRepo: StateRepository,
    private val requestRepository: RequestRepository,
    private val roomCategoryProperties: RoomCategoryProperties,
    private val nextState: State
) : State {
    override fun ask(userId: Int, chatId: Long) {
        stateRepo.execute(SetUserStatus(userId, ROOM_STATE))
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

    override fun handle(update: Update) {
        if (validate(update.text ?: "0", update.chatId)) {
            requestRepository.execute(
                UpdateRequestField(
                    update.userId,
                    Pair("room_type", (update.text?.toInt() ?: 0))
                )
            )
            nextState.ask(update.userId, update.chatId)
        }
    }

    // todo refactor
    private fun validate(text: String, chatId: Long) =
        try {
            roomCategoryProperties.categories.containsKey(text.toInt())
                .also {
                    if (!it) {
                        sendErrorMessage(chatId)
                    }
                }
        } catch (exp: NumberFormatException) {
            sendErrorMessage(chatId)
            false
        }

    private fun sendErrorMessage(chatId: Long) {
        absSender?.execute(SendMessage(chatId, "Неверное значение. Попробуйте еще раз."))
    }
}
