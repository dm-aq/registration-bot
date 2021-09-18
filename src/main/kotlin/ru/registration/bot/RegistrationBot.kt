package ru.registration.bot

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.engine.chat
import ru.registration.bot.engine.user
import ru.registration.bot.engine.userId

private val logger = KotlinLogging.logger {}

@Component
class RegistrationBot(
    @Value("\${bot.username}") private val botName: String,
    @Value("\${bot.token}") private val token: String,
    private val commonFactory: CommonFactory,
    commands: List<RegistrationBotCommand>
) : BaseTelegramLongPollingBot(botName, token) {

    init {
        commands.forEach { register(it) }
    }

    override fun processNonCommandUpdate(update: Update?) {

        if (update?.hasCallbackQuery() == true) {
            answerCallbackQuery(update)
        }

        update?.let {
            update.chat?.let {
                update.user?.let {
                    commonFactory.create(
                        update.userId
                    )?.handle(update, this)
                }
            }
        }
    }

    private fun answerCallbackQuery(update: Update) {
        try {
            this.execute(AnswerCallbackQuery().apply {
                callbackQueryId = update.callbackQuery.id
            })
        } catch (exp: TelegramApiException) {
            logger.error(exp) { "Error occurred during sending answerCallbackQuery" }
        }
    }
}
