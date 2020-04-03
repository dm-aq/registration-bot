package ru.registration.bot.engine.commands.flow

import org.telegram.telegrambots.meta.api.objects.User
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.repositories.specifications.UserRequest

class GoogleSheets(
    private val commonFactory: CommonFactory,
    private val user: User?
)
{
    fun send() {
        val request = commonFactory.requestRepository.query(UserRequest(user?.id)).first()
        commonFactory.googleSheets.send(request)
    }
}