package ru.registration.bot.engine.commands.flow.states

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.engine.commands.flow.State
import ru.registration.bot.engine.commands.flow.StateType.DANCESTYLE_STATE
import ru.registration.bot.engine.commands.flow.StateType.ROOM_STATE
import ru.registration.bot.engine.text
import ru.registration.bot.engine.userId
import ru.registration.bot.repositories.specifications.SetUserStatus
import ru.registration.bot.repositories.specifications.UpdateRequestField

class DanceStyleState(
    private val chat: Chat?,
    private val user: User?,
    private val absSender: AbsSender?,
    private val commonFactory: CommonFactory
) : State {
    override fun ask(userId: Int, chatId: Long) {
        commonFactory.stateRepo.execute(SetUserStatus(userId, ROOM_STATE, DANCESTYLE_STATE))
        absSender?.execute(
            SendMessage(chatId, "Выберите танцевальное направление:")
                .setReplyMarkup(getInlineKeyboard())
        )
    }

    private fun getInlineKeyboard() =
        InlineKeyboardMarkup()
            .setKeyboard(
                commonFactory.danceStyleProperties.values.asSequence()
                    .map { InlineKeyboardButton().setText(it).setCallbackData(it) }
                    .chunked(3)
                    .toList()
            )

    override fun handle(update: Update) {
        val text = update.text ?: ""
        if (validate(text)) {
            commonFactory.requestRepository.execute(UpdateRequestField(update.userId, Pair("dance_type", text)))
            NeighborsState(chat, user, absSender, commonFactory).ask()
        }
    }

    private fun validate(text: String) =
        commonFactory.danceStyleProperties.values.contains(text.toLowerCase())
            .also {
                if (!it) {
                    absSender?.execute(SendMessage(chat?.id, "Неверное значение. Попробуйте еще раз."))
                }
            }
}
