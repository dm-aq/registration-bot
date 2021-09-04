package ru.registration.bot.engine.commands.flow.states

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.chatId
import ru.registration.bot.engine.commands.flow.State
import ru.registration.bot.engine.commands.flow.StateType.FULL_NAME_STATE
import ru.registration.bot.engine.text
import ru.registration.bot.engine.userId
import ru.registration.bot.repositories.RequestRepository
import ru.registration.bot.repositories.StateRepository
import ru.registration.bot.repositories.specifications.SetUserStatus
import ru.registration.bot.repositories.specifications.UpdateRequestField

class FullNameState(
    private val stateRepo: StateRepository,
    private val requestRepository: RequestRepository,
    private val nextState: State
) : State {
    override fun ask(userId: Int, chatId: Long, absSender: AbsSender) {
        stateRepo.execute(SetUserStatus(userId, FULL_NAME_STATE))
        absSender.execute(SendMessage(chatId, "Как вас зовут (фио):"))
    }

    override fun handle(update: Update, absSender: AbsSender) {
        val text = update.text ?: ""
        when (isValid(text)) {
            true -> {
                requestRepository.execute(UpdateRequestField(update.userId, Pair("full_name", text)))
                nextState.ask(update.userId, update.chatId, absSender)
            }

            text.length > 30 ->
                absSender.execute(SendMessage(update.chatId, "Имя слишком длинное."))

            false ->
                absSender.execute(SendMessage(update.chatId, "Что-то не так. Тут нет вашего имени."))
        }
    }

    private fun isValid(text: String?): Boolean =
        text?.isNotBlank() ?: false
}