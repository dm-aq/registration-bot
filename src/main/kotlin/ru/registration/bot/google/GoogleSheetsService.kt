package ru.registration.bot.google

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.ValueRange
import com.google.auth.Credentials
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.ServiceAccountCredentials
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.registration.bot.engine.commands.Request
import java.time.format.DateTimeFormatter
import javax.annotation.PostConstruct

@Service
class GoogleSheetsService(
    @Value("\${services.sheets.spread-sheet-id}") private val spreadsheetId: String,
    @Value("\${services.sheets.range}") private val range: String,
    private val jacksonFactory: JsonFactory
) {

    private lateinit var service: Sheets

    @PostConstruct
    fun init() {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()

        service = Sheets.Builder(httpTransport,
            jacksonFactory, HttpCredentialsAdapter(obtainCredentials())
        )
            .setApplicationName("registration-bot")
            .build()
    }

    private fun obtainCredentials(): Credentials? {
        return ServiceAccountCredentials
            .fromStream(GoogleSheetsService::class.java.getResourceAsStream("/trst-269816-eca0f3ec2434.json"))
            .createScoped(listOf("https://www.googleapis.com/auth/spreadsheets"))
    }

    fun send(request: Request) {
        service.spreadsheets().values()
            .append(spreadsheetId, range,
                ValueRange().setValues(listOf(listOf(
                    request.creationDateTime?.format(dateTimeFormatter),
                    request.email,
                    request.fullName,
                    request.phone,
                    request.roomType,
                    request.danceType,
                    request.neighbors,
                    request.gender,
                    request.telegramLogin
                ))))
            .setValueInputOption("RAW")
            .execute()
    }

    companion object {
        private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
    }
}
