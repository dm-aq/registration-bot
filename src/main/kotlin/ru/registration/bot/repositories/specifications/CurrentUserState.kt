package ru.registration.bot.repositories.specifications

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import ru.registration.bot.engine.commands.flow.StateType
import ru.registration.bot.repositories.QuerySpecification
import java.sql.ResultSet

class CurrentUserState(private val userId: Int?) : QuerySpecification<StateType> {
    override val sql: String
        get() = "select state from requests where user_id = :user_id and state in (:draft_states)"
    override val sqlParameterSource: Map<String?, *>
        get() = MapSqlParameterSource()
            .addValue("user_id", userId)
            .addValue("draft_states",
                listOf(
                    StateType.START_STATE.name,
                    StateType.FULL_NAME_STATE.name,
                    StateType.PHONE_STATE.name,
                    StateType.MAIL_STATE.name,
                    StateType.SEX_STATE.name,
                    StateType.ROOM_STATE.name,
                    StateType.DANCESTYLE_STATE.name,
                    StateType.NEIGHBORS_STATE.name
                )
            )
            .values
    override val rowMapper: RowMapper<StateType>
        get() = RowMapper { rs: ResultSet, _: Int -> StateType.valueOf(rs.getString("state")) }
}