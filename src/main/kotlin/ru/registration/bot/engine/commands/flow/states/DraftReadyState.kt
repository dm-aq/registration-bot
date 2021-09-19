package ru.registration.bot.engine.commands.flow.states

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.MessageService
import ru.registration.bot.engine.chat
import ru.registration.bot.engine.chatId
import ru.registration.bot.engine.commands.RemoveDraftComponent
import ru.registration.bot.engine.commands.flow.State
import ru.registration.bot.engine.commands.flow.StateType.REQUEST_READY
import ru.registration.bot.engine.text
import ru.registration.bot.engine.user
import ru.registration.bot.engine.userId
import ru.registration.bot.repositories.BotRepository
import ru.registration.bot.repositories.specifications.SetUserStatus
import ru.registration.bot.repositories.specifications.UserRequest

class DraftReadyState(
    private val messages: MessageService,
    private val botRepository: BotRepository,
    private val removeDraftComponent: RemoveDraftComponent,
    private val nextState: State
) : State {
    override fun ask(userId: Int, chatId: Long, absSender: AbsSender) {
        botRepository.execute(SetUserStatus(userId, REQUEST_READY))
        val request = botRepository.query(UserRequest(userId, REQUEST_READY)).first()

        val message = """
            ${messages.getMessage("draft_header")}
            
            ${messages.getMessage("draft_phone")} ${request.phone}
            ${messages.getMessage("draft_email")} ${request.email}
            ${messages.getMessage("draft_fullname")} ${request.fullName}
            ${messages.getMessage("draft_gender")} ${request.gender}
            ${messages.getMessage("draft_room")} ${request.roomType}
            ${messages.getMessage("draft_dance_style")} ${request.danceType}
            ${messages.getMessage("draft_neighbors")} ${request.neighbors}
        """.trimIndent()
        absSender.execute(SendMessage(chatId, message).setReplyMarkup(getInlineKeyboard()))
    }

    private fun getInlineKeyboard(): InlineKeyboardMarkup =
        InlineKeyboardMarkup()
            .setKeyboard(
                listOf(
                    listOf(InlineKeyboardButton()
                        .setText(messages.getMessage("draft_send_btn")).setCallbackData("send")),
                    listOf(InlineKeyboardButton()
                        .setText(messages.getMessage("draft_remove_btn")).setCallbackData("remove"))
                )
            )

    override fun handle(update: Update, absSender: AbsSender) {

        when (update.text ?: "") {
            "send" ->
                nextState.handle(update, absSender)
            "remove" ->
                update.user?.let {
                    update.chat?.let {
                        removeDraftComponent.removeDraft(update.userId, update.chatId, absSender)
                    }
                }
        }
    }
}
