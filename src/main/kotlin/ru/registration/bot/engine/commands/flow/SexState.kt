package ru.registration.bot.engine.commands.flow

import mu.KotlinLogging
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.engine.commands.Emoji
import ru.registration.bot.repositories.specifications.SetUserStatus
import ru.registration.bot.repositories.specifications.UpdateRequestField

private val logger = KotlinLogging.logger {}

class SexState(
    private val chat: Chat?,
    private val user: User?,
    private val absSender: AbsSender?,
    private val commonFactory: CommonFactory
) : State {
    override fun ask() {
        commonFactory.stateRepo.execute(SetUserStatus(user?.id, StateType.MAIL_STATE, StateType.SEX_STATE))
        absSender?.execute(SendMessage(chat?.id, "Пол:").setReplyMarkup(createInlineKeyboard()))
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

    override fun handle(text: String?) {
        try {
            val sex: Sex = Sex.parse(text ?: "")
            commonFactory.requestRepository.execute(UpdateRequestField(user?.id, Pair("sex", sex.value)))
            RoomCategoryState(chat, user, absSender, commonFactory).ask()
        } catch (exp: IllegalArgumentException) {
            logger.warn(exp) { "Wrong gender value" }
            absSender?.execute(SendMessage(chat?.id, "Попробуйте еще раз."))
        }
    }
}

enum class Sex(val value: String) {
    MALE("М"), FEMALE("Ж");

    companion object {
        fun parse(text: String): Sex =
            when (text.toUpperCase()) {
                "M", "MALE", "М", "МУЖЧИНА" -> MALE
                "F", "FEMALE", "Ж", "ЖЕНЩИНА" -> FEMALE
                else -> throw IllegalArgumentException("Illegal sex value: $text")
            }
    }
}
