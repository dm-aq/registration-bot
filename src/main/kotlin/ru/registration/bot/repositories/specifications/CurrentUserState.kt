package ru.registration.bot.repositories.specifications

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import ru.registration.bot.engine.commands.flow.StateType
import ru.registration.bot.repositories.QuerySpecification
import java.sql.ResultSet

class CurrentUserState(private val userId: Int?) : QuerySpecification<StateType> {
    override val sql: String
        get() = "select state from states where user_id = :user_id"
    override val sqlParameterSource: Map<String?, *>
        get() = MapSqlParameterSource("user_id", userId).values
    override val rowMapper: RowMapper<StateType>
        get() = RowMapper<StateType> { rs: ResultSet, i: Int -> StateType.getByValue(rs.getInt("state")) }
}