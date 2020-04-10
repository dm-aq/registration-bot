package ru.registration.bot.repositories.specifications

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import ru.registration.bot.engine.commands.flow.StateType
import ru.registration.bot.repositories.QuerySpecification
import java.sql.ResultSet

class CurrentUserState(private val userId: Int?) : QuerySpecification<StateType> {
    override val sql: String
        get() = "select state from requests where user_id = :user_id and state <> :finishedState"
    override val sqlParameterSource: Map<String?, *>
        get() = MapSqlParameterSource()
            .addValue("user_id", userId)
            .addValue("finishedState", StateType.EXPORTED.state)
            .values
    override val rowMapper: RowMapper<StateType>
        get() = RowMapper<StateType> { rs: ResultSet, i: Int -> StateType.getByValue(rs.getInt("state")) }
}