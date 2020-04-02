package ru.registration.bot.engine.commands.flow

import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import org.telegram.telegrambots.meta.api.objects.User
import ru.registration.bot.engine.CommonFactory
import ru.registration.bot.repositories.specifications.UserRequest

class GoogleSheets(
    private val commonFactory: CommonFactory,
    private val user: User?
)
{
    companion object {
        val jacksonFactory: JsonFactory = JacksonFactory.getDefaultInstance()
    }

    fun send() {
        val request = commonFactory.requestRepository.query(UserRequest(user?.id)).first()
        commonFactory.googleSheets.send(request)
    }
}