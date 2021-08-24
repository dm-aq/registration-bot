package ru.registration.bot.engine.commands.flow.states

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.engine.chatId
import ru.registration.bot.engine.commands.flow.Node
import ru.registration.bot.engine.commands.flow.PersistedState
import ru.registration.bot.engine.commands.flow.StateType.PHONE_STATE
import ru.registration.bot.engine.text
import ru.registration.bot.engine.userId
import ru.registration.bot.repositories.specifications.SetUserStatus
import ru.registration.bot.repositories.specifications.UpdateRequestField

class PhoneNumberState(
    private val chat: Chat?,
    private val user: User?,
    private val absSender: AbsSender?,
    private val commonFactory: CommonFactory,
    prevState: PersistedState,
    nextState: PersistedState
) : PersistedState, Node<PersistedState> {

    override val type = PHONE_STATE

    override val prev = prevState

    override val next = nextState

    override fun ask(userId: Int, chatId: Long) {
        commonFactory.stateRepo.execute(SetUserStatus(userId, prev.type, type))
        absSender?.execute(SendMessage(chatId, "Введите ваш номер телефона:"))
    }

    override fun handle(update: Update) {
        val text = update.text ?: ""
        if (validate(text)) {
            commonFactory.requestRepository.execute(UpdateRequestField(update.userId, Pair("phone", text)))
            FullNameState(chat, user, absSender, commonFactory).ask(update.userId, update.chatId)
        } else {
            absSender?.execute(SendMessage(update.chatId, """
                Номер телефона должен быть в следующем формате: 8XXXYYYZZZZ
            """.trimIndent()))
        }
    }

    private fun validate(text: String): Boolean = !text.matches(phoneRegEx)

    companion object {
        private val phoneRegEx = "(8)\\d{3}\\d{3}-?\\d{4}".toRegex()
    }
}
