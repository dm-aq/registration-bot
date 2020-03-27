package ru.registration.bot.engine.commands

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.engine.commands.flow.StateType
import ru.registration.bot.repositories.specifications.SetUserStatus

class RemoveDraftCommand(
    private val commonFactory: CommonFactory
) : BotCommand("/remove_draft", "Remove draft") {

    override fun execute(absSender: AbsSender?, user: User?, chat: Chat?, arguments: Array<out String>?) {

        when(commonFactory.currentUserStateType(user)){
            StateType.PHONE_STATE -> removeDraft(user, chat, absSender)
            StateType.FULL_NAME_STATE -> removeDraft(user, chat, absSender)
            StateType.SEX_STATE -> removeDraft(user, chat, absSender)
            StateType.DANCESTYLE_STATE -> removeDraft(user, chat, absSender)
            StateType.ROOM_STATE -> removeDraft(user, chat, absSender)
            StateType.NEIGHBORS_STATE -> removeDraft(user, chat, absSender)
            else -> sendWarningMessage(chat, absSender)
        }
    }

    private fun removeDraft(user: User?, chat: Chat?, absSender: AbsSender?){
        //            todo удалить данные из бд
//            stateFactory.stateRepo.execute(RemoveRegistrationRequest(..))
        commonFactory.stateRepo.execute(SetUserStatus(user?.id, StateType.PHONE_STATE))
        absSender?.execute(SendMessage(chat?.id, "Черновик удален."))
    }

    private fun sendWarningMessage(chat: Chat?, absSender: AbsSender?){
        absSender?.execute(SendMessage(chat?.id, "У вас нет черновика."))
    }
}