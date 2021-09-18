package ru.registration.bot.engine.commands

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.MessageService
import ru.registration.bot.RegistrationBotCommand
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.engine.commands.flow.states.DanceStyleState
import ru.registration.bot.engine.commands.flow.states.FullNameState
import ru.registration.bot.engine.commands.flow.states.GenderState
import ru.registration.bot.engine.commands.flow.states.NeighborsState
import ru.registration.bot.engine.commands.flow.states.RoomCategoryState

@Component
class RegistrationCommand(
    @Value("\${registration-closed:false}") private val registrationClosed: Boolean,
    private val messages: MessageService,
    private val commonFactory: CommonFactory
) : RegistrationBotCommand {

    override fun getCommandIdentifier() = "new_registration"

    override fun getDescription() = ""

    override fun processMessage(absSender: AbsSender?, message: Message?, arguments: Array<out String>?) {
        absSender?.let {
            message?.let {
                execute(absSender, message.from, message.chat)
            }
        }
    }

    private fun execute(absSender: AbsSender, user: User, chat: Chat) {

        if (registrationClosed) {
            absSender.execute(SendMessage(chat.id, messages.getMessage("registration_command_close")))
            return
        }

        val currentState = commonFactory.create(user.id)

        when (currentState) {
            is FullNameState -> sendContinueMessage(absSender, chat)
            is GenderState -> sendContinueMessage(absSender, chat)
            is RoomCategoryState -> sendContinueMessage(absSender, chat)
            is DanceStyleState -> sendContinueMessage(absSender, chat)
            is NeighborsState -> sendContinueMessage(absSender, chat)
        }

        currentState?.ask(user.id, chat.id, absSender)
    }

    private fun sendContinueMessage(absSender: AbsSender, chat: Chat) {
        absSender.execute(SendMessage(chat.id, messages.getMessage("registration_command_draft")))
    }
}
