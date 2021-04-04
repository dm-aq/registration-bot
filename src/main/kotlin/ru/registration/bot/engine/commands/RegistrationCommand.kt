package ru.registration.bot.engine.commands

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.RegistrationBotCommand
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.engine.commands.flow.DanceStyleState
import ru.registration.bot.engine.commands.flow.FullNameState
import ru.registration.bot.engine.commands.flow.NeighborsState
import ru.registration.bot.engine.commands.flow.RoomCategoryState
import ru.registration.bot.engine.commands.flow.SexState

@Component
class RegistrationCommand(
    @Value("\${registration-closed:false}") private val registrationClosed: Boolean,
    private val commonFactory: CommonFactory
) : RegistrationBotCommand {

    override fun getCommandIdentifier() = "new_registration"

    override fun getDescription() = ""

    override fun processMessage(absSender: AbsSender?, message: Message?, arguments: Array<out String>?) =
        execute(absSender, message!!.from, message.chat, arguments)

    private fun execute(absSender: AbsSender?, user: User?, chat: Chat?, arguments: Array<out String>?) {

        if (registrationClosed){
            absSender?.execute(SendMessage(chat?.id, "Регистрация на выезд закрыта"))
            return
        }

        val currentState = commonFactory.create(chat, user, absSender)

        when (currentState) {
            is FullNameState -> sendContinueMessage(absSender, chat)
            is SexState -> sendContinueMessage(absSender, chat)
            is RoomCategoryState -> sendContinueMessage(absSender, chat)
            is DanceStyleState -> sendContinueMessage(absSender, chat)
            is NeighborsState -> sendContinueMessage(absSender, chat)
        }

        currentState.ask()
    }

    private fun sendContinueMessage(absSender: AbsSender?, chat: Chat?) {
        absSender?.execute(SendMessage(chat?.id, "Давайте заполним оставшиеся поля в черновике."))
    }
}
