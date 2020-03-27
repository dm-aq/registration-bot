package ru.registration.bot.engine.commands.flow

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.repositories.specifications.SetUserStatus
import ru.registration.bot.repositories.specifications.UpdateRequestField

class PhoneNumberState(
    private val chat: Chat?,
    private val user: User?,
    private val absSender: AbsSender?,
    private val commonFactory: CommonFactory
): State {

    override fun ask(){
        commonFactory.stateRepo.execute(SetUserStatus(user?.id, StateType.PHONE_STATE))
        absSender?.execute(SendMessage(chat?.id, "Введите ваш номер телефона:"))
    }

    override fun handle(text: String?){
        // todo validate
        commonFactory.requestRepository.execute(UpdateRequestField(user?.id, Pair("phone", text ?: "")))
        FullNameState(chat, user, absSender, commonFactory).ask()
    }
}