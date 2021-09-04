package ru.registration.bot.engine.commands

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.commands.Emoji.POINT_FINGER_RIGHT
import ru.registration.bot.engine.commands.flow.StateType.PHONE_STATE
import ru.registration.bot.repositories.StateRepository
import ru.registration.bot.repositories.specifications.SetUserStatus

@Component
class RemoveDraftComponent(
    private val stateRepo: StateRepository
) {

    fun removeDraft(user: User, chat: Chat, absSender: AbsSender) {
        stateRepo.execute(SetUserStatus(user.id, PHONE_STATE))
        absSender.execute(SendMessage(chat.id, "Черновик удален."))
        absSender.execute(
            SendMessage(
                chat.id,
                "Для того, чтобы заполнить заявку еще раз нажмите " +
                    "$POINT_FINGER_RIGHT /new_registration"
            )
        )
    }
}
