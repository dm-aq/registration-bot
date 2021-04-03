package ru.registration.bot.engine.commands

import java.time.LocalDateTime

// todo refactor
class Request private constructor(
    val requestId: Int?,
    val telegramLogin: String?,
    val phone: String?,
    val fullName: String?,
    val email: String?,
    val sex: String?,
    val roomType: Int?,
    val danceType: String?,
    val neighbors: String?,
    val creationDateTime: LocalDateTime?
) {

    data class Builder(
        var requestId: Int? = null,
        var telegramLogin: String? = null,
        var phone: String? = null,
        var fullName: String? = null,
        var email: String? = null,
        var sex: String? = null,
        var roomType: Int? = null,
        var danceType: String? = null,
        var neighbors: String? = null,
        var creationDateTime: LocalDateTime? = null
    ) {
        fun requestId(requestId: Int) = apply { this.requestId = requestId }
        fun telegramLogin(telegramLogin: String) = apply { this.telegramLogin = telegramLogin }
        fun phone(phone: String) = apply { this.phone = phone }
        fun fullName(fullName: String) = apply { this.fullName = fullName }
        fun email(email: String) = apply { this.email = email }
        fun sex(sex: String) = apply { this.sex = sex }
        fun roomType(roomType: Int) = apply { this.roomType = roomType }
        fun danceType(danceType: String) = apply { this.danceType = danceType }
        fun neighbors(neighbors: String) = apply { this.neighbors = neighbors }
        fun creationDateTime(creationDateTime: LocalDateTime) = apply { this.creationDateTime = creationDateTime }

        fun build() =
            Request(
                requestId,
                telegramLogin,
                phone,
                fullName,
                email,
                sex,
                roomType,
                danceType,
                neighbors,
                creationDateTime
            )
    }
}
