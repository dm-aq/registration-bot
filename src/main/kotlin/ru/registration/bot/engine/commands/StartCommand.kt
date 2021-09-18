package ru.registration.bot.engine.commands

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.RegistrationBotCommand

@Component
class StartCommand : RegistrationBotCommand {

    override fun getCommandIdentifier() = "start"

    override fun getDescription() = ""

    override fun processMessage(absSender: AbsSender?, message: Message?, arguments: Array<out String>?) {
        absSender?.let {
            message?.let {
                execute(absSender, message.chat)
            }
        }
    }

    private fun execute(absSender: AbsSender, chat: Chat) {

        absSender.execute(SendMessage(chat.id, """
            Очень скоро состоится наш ежегодный танцевальный выезд. Я помогу вам зарегистрироваться.
            
            Для того, чтобы начать регистрацию нажмите 
            ${Emoji.POINT_FINGER_RIGHT} /new_registration ${Emoji.POINT_FINGER_LEFT}
            Если вы ошиблись просто удалите черновик на любом шаге 
            ${Emoji.POINT_FINGER_RIGHT} /remove_draft ${Emoji.POINT_FINGER_LEFT}
        """.trimIndent())
            .setReplyMarkup(registrationButton())
        )
    }

    private fun registrationButton(): InlineKeyboardMarkup =
        InlineKeyboardMarkup().setKeyboard(
            listOf(
                listOf(
                    InlineKeyboardButton()
                        .setText("Начать регистрацию")
                        .setCallbackData("new_registration")
                )
            )
        )
}
