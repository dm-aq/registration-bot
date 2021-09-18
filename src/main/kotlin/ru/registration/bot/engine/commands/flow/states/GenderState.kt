package ru.registration.bot.engine.commands.flow.states

import mu.KotlinLogging
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.chatId
import ru.registration.bot.engine.commands.Emoji
import ru.registration.bot.engine.commands.flow.State
import ru.registration.bot.engine.commands.flow.StateType.GENDER_STATE
import ru.registration.bot.engine.text
import ru.registration.bot.engine.userId
import ru.registration.bot.repositories.BotRepository
import ru.registration.bot.repositories.specifications.SetUserStatus
import ru.registration.bot.repositories.specifications.UpdateRequestField

private val logger = KotlinLogging.logger {}

class GenderState(
    private val botRepository: BotRepository,
    private val nextState: State
) : State {
    override fun ask(userId: Int, chatId: Long, absSender: AbsSender) {
        botRepository.execute(SetUserStatus(userId, GENDER_STATE))
        absSender.execute(SendMessage(chatId, "Пол:").setReplyMarkup(createInlineKeyboard()))
    }

    private fun createInlineKeyboard() =
        InlineKeyboardMarkup()
            .setKeyboard(
                listOf(
                    listOf(
                        InlineKeyboardButton().setText(Emoji.DANCING_MAN.toString()).setCallbackData("М"),
                        InlineKeyboardButton().setText(Emoji.DANCING_WOMAN.toString()).setCallbackData("Ж")
                    )
                )
            )

    override fun handle(update: Update, absSender: AbsSender) {
        try {
            val gender: Gender = Gender.parse(update.text ?: "")
            botRepository.execute(UpdateRequestField(update.userId, Pair("gender", gender.value)))
            nextState.ask(update.userId, update.chatId, absSender)
        } catch (exp: IllegalArgumentException) {
            logger.warn(exp) { "Wrong gender value" }
            absSender.execute(SendMessage(update.chatId, "Попробуйте еще раз."))
        }
    }
}

enum class Gender(val value: String) {
    MALE("М"), FEMALE("Ж");

    companion object {
        fun parse(text: String): Gender =
            when (text.toUpperCase()) {
                "M", "MALE", "М", "МУЖЧИНА" -> MALE
                "F", "FEMALE", "Ж", "ЖЕНЩИНА" -> FEMALE
                else -> throw IllegalArgumentException("Illegal gender value: $text")
            }
    }
}
