package ru.registration.bot.engine.commands.flow.states

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.chatId
import ru.registration.bot.engine.commands.flow.State
import ru.registration.bot.engine.commands.flow.StateType.PHONE_STATE
import ru.registration.bot.engine.text
import ru.registration.bot.engine.userId
import ru.registration.bot.repositories.RequestRepository
import ru.registration.bot.repositories.StateRepository
import ru.registration.bot.repositories.specifications.SetUserStatus
import ru.registration.bot.repositories.specifications.UpdateRequestField

class PhoneNumberState(
    private val absSender: AbsSender,
    private val stateRepo: StateRepository,
    private val requestRepository: RequestRepository,
    private val nextState: State
) : State {

    override fun ask(userId: Int, chatId: Long) {
        stateRepo.execute(SetUserStatus(userId, PHONE_STATE))
        absSender.execute(SendMessage(chatId, "Введите ваш номер телефона:"))
    }

    override fun handle(update: Update) {
        val text = update.text ?: ""
        if (validate(text)) {
            requestRepository.execute(UpdateRequestField(update.userId, Pair("phone", text)))
            nextState.ask(update.userId, update.chatId)
        } else {
            absSender.execute(SendMessage(update.chatId, """
                Номер телефона должен быть в следующем формате: 8XXXYYYZZZZ
            """.trimIndent()))
        }
    }

    private fun validate(text: String): Boolean = !text.matches(phoneRegEx)

    companion object {
        private val phoneRegEx = "(8)\\d{3}\\d{3}-?\\d{4}".toRegex()
    }
}
