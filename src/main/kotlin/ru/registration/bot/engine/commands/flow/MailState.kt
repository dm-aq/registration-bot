package ru.registration.bot.engine.commands.flow

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.repositories.specifications.SetUserStatus
import ru.registration.bot.repositories.specifications.UpdateRequestField

class MailState(
    private val chat: Chat?,
    private val user: User?,
    private val absSender: AbsSender?,
    private val commonFactory: CommonFactory
): State {
    override fun ask() {
        commonFactory.stateRepo.execute(SetUserStatus(user?.id, StateType.FULL_NAME_STATE, StateType.MAIL_STATE))
        absSender?.execute(SendMessage(chat?.id, "Адрес электронной почты:"))
    }

    override fun handle(text: String?){
        if(validation(text ?: "")) {
            commonFactory.requestRepository.execute(UpdateRequestField(user?.id, Pair("email", text ?: "")))
            SexState(chat, user, absSender, commonFactory).ask()
        }
    }

    private fun validation(text: String): Boolean{
        if (!"^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}\$"
                .toRegex(RegexOption.IGNORE_CASE).matches(text)){
            absSender?.execute(SendMessage(chat?.id, "Адрес какой-то не такой :("))
            return false
        }
        return true
    }
}