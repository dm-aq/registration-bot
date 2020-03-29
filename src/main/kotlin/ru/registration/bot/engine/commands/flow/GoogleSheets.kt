package ru.registration.bot.engine.commands.flow

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.ValueRange
import com.google.auth.Credentials
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.ServiceAccountCredentials
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

        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
        val spreadsheetId = "1qdZpcyTdCnNFsexLeMDvmmjgNhgqcITwXjXvdu2fW40"
        val range = "List1!A2:D"

        val service = Sheets.Builder(httpTransport, jacksonFactory, HttpCredentialsAdapter(obtainCredentials()))
            .setApplicationName("registration-bot")
            .build()

        val result =  service.spreadsheets().values().append(spreadsheetId, range,
            ValueRange().setValues(listOf(listOf(request.phone, request.fullName))))
            .execute()

        // todo check the result
    }

    private fun obtainCredentials(): Credentials? {
        return ServiceAccountCredentials
            .fromStream(GoogleSheets::class.java.getResourceAsStream("/trst-269816-eca0f3ec2434.json"))
            .createScoped(listOf("https://www.googleapis.com/auth/spreadsheets.readonly"))
    }
}