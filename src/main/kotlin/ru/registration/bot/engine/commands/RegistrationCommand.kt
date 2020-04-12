package ru.registration.bot.engine.commands

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.engine.commands.flow.*

class RegistrationCommand(
    private val commonFactory: CommonFactory,
    private val allowedUsers: Set<String>
    ) : BotCommand("/new_registration", "Start new registration") {

    override fun execute(absSender: AbsSender?, user: User?, chat: Chat?, arguments: Array<out String>?) {

        if (!allowedUsers.contains(user?.userName)){
            absSender?.execute(SendMessage(chat?.id, "Вам пока сюда нельзя :("))
            return
        }

        val currentState = commonFactory.create(chat, user, absSender)

        when(currentState){
            is FullNameState -> sendContinueMessage(absSender, chat)
            is SexState -> sendContinueMessage(absSender, chat)
            is RoomCategoryState -> sendContinueMessage(absSender, chat)
            is DanceStyleState -> sendContinueMessage(absSender, chat)
            is NeighborsState -> sendContinueMessage(absSender, chat)
        }

        currentState.ask()
    }

    private fun sendContinueMessage(absSender: AbsSender?, chat: Chat?){
        absSender?.execute(SendMessage(chat?.id, "Давайте заполним оставшиеся поля в черновике."))
    }
}