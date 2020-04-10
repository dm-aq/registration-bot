package ru.registration.bot.repositories.specifications

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import ru.registration.bot.engine.commands.Request
import ru.registration.bot.engine.commands.flow.StateType
import ru.registration.bot.repositories.QuerySpecification

class UserRequest(
    private val userId: Int?,
    private val state: StateType
) : QuerySpecification<Request> {
    override val sql: String
        get() = "select id, full_name, phone, sex, room_type, dance_type, neighbors, updstmp " +
                "from requests " +
                "where user_id = :user_id and state = :state order by updstmp desc"
    override val sqlParameterSource: Map<String?, *>
        get() = MapSqlParameterSource()
            .addValue("user_id", userId)
            .addValue("state", state.state)
            .values
    override val rowMapper: RowMapper<Request>
        get() = RowMapper<Request> {
                rs, _ ->
            Request.Builder()
                .requestId(rs.getInt("id"))
                .fullName(rs.getString("full_name"))
                .phone(rs.getString("phone"))
                .sex(rs.getString("sex"))
                .roomType(rs.getInt("room_type"))
                .danceType(rs.getString("dance_type"))
                .neighbors(rs.getString("neighbors"))
                .creationDateTime(rs.getTimestamp("updstmp").toLocalDateTime())
                .build()
        }
}
