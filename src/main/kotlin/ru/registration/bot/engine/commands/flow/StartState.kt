package ru.registration.bot.engine.commands.flow

import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.engine.text
import ru.registration.bot.repositories.specifications.InitUserRequest

class StartState(
    private val chat: Chat?,
    private val user: User?,
    private val absSender: AbsSender?,
    private val commonFactory: CommonFactory
) : State {
    override fun ask() {
        commonFactory.requestRepository.execute(InitUserRequest(user, StateType.START_STATE))

        PhoneNumberState(chat, user, absSender, commonFactory).ask()
    }

    override fun handle(update: Update?) {
        if (update?.text == "new_registration") {
            ask()
        }
    }
}
