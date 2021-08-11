package ru.registration.bot.engine.commands

import java.time.LocalDateTime

data class Request(
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
)
