package ru.registration.bot.repositories.specifications

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import ru.registration.bot.engine.commands.flow.StateType
import ru.registration.bot.repositories.ExecSpecification

class InitUserStatus(private val id: Int?, private val state: StateType) :
    ExecSpecification {
    override val sql: String
        get() = "insert into states(user_id, state) values(:user_id, :state)"
    override val sqlParameterSource: Map<String, *>
        get() =
            MapSqlParameterSource()
                .addValue("user_id", id)
                .addValue("state", state.state)
                .values

}
