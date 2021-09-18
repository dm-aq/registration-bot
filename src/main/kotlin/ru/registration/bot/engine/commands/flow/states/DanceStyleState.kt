package ru.registration.bot.engine.commands.flow.states

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.configuration.DanceStyleProperties
import ru.registration.bot.engine.chatId
import ru.registration.bot.engine.commands.flow.State
import ru.registration.bot.engine.commands.flow.StateType.DANCESTYLE_STATE
import ru.registration.bot.engine.text
import ru.registration.bot.engine.userId
import ru.registration.bot.repositories.BotRepository
import ru.registration.bot.repositories.specifications.SetUserStatus
import ru.registration.bot.repositories.specifications.UpdateRequestField

class DanceStyleState(
    private val botRepository: BotRepository,
    private val danceStyleProperties: DanceStyleProperties,
    private val nextState: State
) : State {
    override fun ask(userId: Int, chatId: Long, absSender: AbsSender) {
        botRepository.execute(SetUserStatus(userId, DANCESTYLE_STATE))
        absSender.execute(
            SendMessage(chatId, "Выберите танцевальное направление:")
                .setReplyMarkup(getInlineKeyboard())
        )
    }

    private fun getInlineKeyboard() =
        InlineKeyboardMarkup()
            .setKeyboard(
                danceStyleProperties.values.asSequence()
                    .map { InlineKeyboardButton().setText(it).setCallbackData(it) }
                    .chunked(3)
                    .toList()
            )

    override fun handle(update: Update, absSender: AbsSender) {
        val text = update.text ?: ""
        if (text.isValid()) {
            botRepository.execute(UpdateRequestField(update.userId, Pair("dance_type", text)))
            nextState.ask(update.userId, update.chatId, absSender)
        } else {
            absSender.execute(SendMessage(update.chatId, "Неверное значение. Попробуйте еще раз."))
        }
    }

    private fun String.isValid(): Boolean = danceStyleProperties.values.contains(this.toLowerCase())
}
