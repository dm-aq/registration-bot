package ru.registration.bot.engine.commands.flow

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.repositories.specifications.SetUserStatus
import ru.registration.bot.repositories.specifications.UpdateRequestField

class RoomCategoryState(
    private val chat: Chat?,
    private val user: User?,
    private val absSender: AbsSender?,
    private val commonFactory: CommonFactory
): State {
    override fun ask() {
        commonFactory.stateRepo.execute(SetUserStatus(user?.id, StateType.ROOM_STATE))
        absSender?.execute(SendMessage(chat?.id, "Категория номера:"))
    }

    override fun handle(text: String?) {
        // todo validate
        commonFactory.requestRepository.execute(UpdateRequestField(user?.id, Pair("room_type", (text?.toInt() ?: 0))))
        DanceStyleState(chat, user, absSender, commonFactory).ask()
    }

}
