package ru.registration.bot.engine.commands

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.MessageService
import ru.registration.bot.RegistrationBotCommand

@Component
class StartCommand(
    private val messages: MessageService
) : RegistrationBotCommand {

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
            ${messages.getMessage("greeting")}
            
            ${messages.getMessage("greeting_new_reg")} 
            ${Emoji.POINT_FINGER_RIGHT} /new_registration ${Emoji.POINT_FINGER_LEFT}
            ${messages.getMessage("greeting_remove_draft")}  
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
                        .setText(messages.getMessage("new_reg_btn"))
                        .setCallbackData("new_registration")
                )
            )
        )
}
