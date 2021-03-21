package ru.registration.bot.engine.commands

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.engine.commands.flow.DanceStyleState
import ru.registration.bot.engine.commands.flow.FullNameState
import ru.registration.bot.engine.commands.flow.NeighborsState
import ru.registration.bot.engine.commands.flow.RoomCategoryState
import ru.registration.bot.engine.commands.flow.SexState

class RegistrationCommand(
    private val commonFactory: CommonFactory
) : BotCommand("/new_registration", "Start new registration") {

    override fun execute(absSender: AbsSender?, user: User?, chat: Chat?, arguments: Array<out String>?) {

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
