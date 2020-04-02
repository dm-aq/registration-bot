package ru.registration.bot.repositories.specifications

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import ru.registration.bot.engine.commands.Request
import ru.registration.bot.repositories.QuerySpecification

class UserRequest(
    private val userId: Int?
) : QuerySpecification<Request> {
    override val sql: String
        get() = "select full_name, phone, sex, room_type, dance_type, neighbors from requests where user_id = :user_id"
    override val sqlParameterSource: Map<String?, *>
        get() = MapSqlParameterSource("user_id", userId).values
    override val rowMapper: RowMapper<Request>
        get() = RowMapper<Request> {
                rs, rowNum ->
            Request.Builder()
                .fullName(rs.getString("full_name"))
                .phone(rs.getString("phone"))
                .sex(rs.getString("sex"))
                .roomType(rs.getInt("room_type"))
                .danceType(rs.getString("dance_type"))
                .neighbors(rs.getString("neighbors"))
                .build()
        }
}
