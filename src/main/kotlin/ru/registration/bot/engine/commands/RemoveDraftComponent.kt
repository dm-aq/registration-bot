package ru.registration.bot.engine.commands

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.commands.Emoji.POINT_FINGER_RIGHT
import ru.registration.bot.engine.commands.flow.StateType.PHONE_STATE
import ru.registration.bot.repositories.StateRepository
import ru.registration.bot.repositories.specifications.SetUserStatus

@Component
class RemoveDraftComponent(
    private val stateRepo: StateRepository
) {

    fun removeDraft(userId: Int, chatId: Long, absSender: AbsSender) {
        stateRepo.execute(SetUserStatus(userId, PHONE_STATE))
        absSender.execute(SendMessage(chatId, "Черновик удален."))
        absSender.execute(
            SendMessage(
                chatId,
                "Для того, чтобы заполнить заявку еще раз нажмите " +
                    "$POINT_FINGER_RIGHT /new_registration"
            )
        )
    }
}
