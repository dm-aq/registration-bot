package ru.registration.bot.engine.commands.flow

import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.repositories.specifications.InitUserRequest
import ru.registration.bot.repositories.specifications.InitUserStatus

class StartState(
    private val chat: Chat?,
    private val user: User?,
    private val absSender: AbsSender?,
    private val commonFactory: CommonFactory
): State {
    override fun ask() {
        commonFactory.stateRepo.execute(InitUserStatus(user?.id, StateType.START_STATE))
        commonFactory.requestRepository.execute(InitUserRequest(user?.id))

        PhoneNumberState(chat, user, absSender, commonFactory).ask()
    }

    override fun handle(text: String?) {}
}