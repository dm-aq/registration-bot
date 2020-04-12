package ru.registration.bot.engine.commands.flow

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.repositories.specifications.SetUserStatus
import ru.registration.bot.repositories.specifications.UpdateRequestField

class FullNameState(
    private val chat: Chat?,
    private val user: User?,
    private val absSender: AbsSender?,
    private val commonFactory: CommonFactory
): State {
    override fun ask() {
        commonFactory.stateRepo.execute(SetUserStatus(user?.id, StateType.PHONE_STATE, StateType.FULL_NAME_STATE))
        absSender?.execute(SendMessage(chat?.id, "Как вас зовут:"))
    }

    override fun handle(text: String?) {

        if (validate(text)) {
            commonFactory.requestRepository.execute(UpdateRequestField(user?.id, Pair("full_name", text ?: "")))
            MailState(chat, user, absSender, commonFactory).ask()
        }
    }

    private fun validate(text: String?): Boolean {
        if(text.isNullOrEmpty() || text.isBlank()){
            absSender?.execute(SendMessage(chat?.id, "Что-то не так. Тут нет вашего имени."))
            return false
        }else if(text.length > 30){
            absSender?.execute(SendMessage(chat?.id, "Имя слишком длинное."))
            return false
        }

        return true
    }
}