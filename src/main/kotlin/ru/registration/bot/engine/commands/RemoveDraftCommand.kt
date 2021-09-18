package ru.registration.bot.engine.commands

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.RegistrationBotCommand
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.engine.commands.flow.StateType.DANCESTYLE_STATE
import ru.registration.bot.engine.commands.flow.StateType.FULL_NAME_STATE
import ru.registration.bot.engine.commands.flow.StateType.MAIL_STATE
import ru.registration.bot.engine.commands.flow.StateType.NEIGHBORS_STATE
import ru.registration.bot.engine.commands.flow.StateType.PHONE_STATE
import ru.registration.bot.engine.commands.flow.StateType.REQUEST_READY
import ru.registration.bot.engine.commands.flow.StateType.ROOM_STATE
import ru.registration.bot.engine.commands.flow.StateType.SEX_STATE

@Component
class RemoveDraftCommand(
    private val commonFactory: CommonFactory,
    private val removeDraftComponent: RemoveDraftComponent
) : RegistrationBotCommand {

    override fun getCommandIdentifier() = "remove_draft"

    override fun getDescription() = ""

    override fun processMessage(absSender: AbsSender?, message: Message?, arguments: Array<out String>?) {
        absSender?.let {
            message?.let {
                execute(absSender, message.from, message.chat)
            }
        }
    }

    fun execute(absSender: AbsSender, user: User, chat: Chat) {

        when (commonFactory.currentUserStateType(user.id)) {
            PHONE_STATE, FULL_NAME_STATE, MAIL_STATE,
            SEX_STATE, DANCESTYLE_STATE, ROOM_STATE,
            NEIGHBORS_STATE, REQUEST_READY -> removeDraftComponent.removeDraft(user.id, chat.id, absSender)
            else -> sendWarningMessage(chat, absSender)
        }
    }

    private fun sendWarningMessage(chat: Chat, absSender: AbsSender) {
        absSender.execute(SendMessage(chat.id, "У вас нет черновика."))
    }
}
