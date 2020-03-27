package ru.registration.bot.engine.commands

class Request private constructor(
    val userId: Int?,
    val phone: String?,
    val fullName: String?,
    val sex: String?,
    val roomType: Int?,
    val danceType: String?,
    val neighbors: String?
){

    data class Builder(
        var userId: Int? = null,
        var phone: String? = null,
        var fullName: String? = null,
        var sex: String? = null,
        var roomType: Int? = null,
        var danceType: String? = null,
        var neighbors: String? = null
    ){
        fun userId(userId: Int) = apply { this.userId = userId }
        fun phone(phone: String) = apply { this.phone = phone }
        fun fullName(fullName: String) = apply { this.fullName = fullName }
        fun sex(sex: String) = apply { this.sex = sex }
        fun roomType(roomType: Int) = apply { this.roomType = roomType }
        fun danceType(danceType: String) = apply { this.danceType = danceType }
        fun neighbors(neighbors: String) = apply { this.neighbors = neighbors }

        fun build() = Request(userId, phone, fullName, sex, roomType, danceType, neighbors)
    }
}