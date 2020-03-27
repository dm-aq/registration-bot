package ru.registration.bot.repositories.specifications

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import ru.registration.bot.engine.commands.flow.StateType
import ru.registration.bot.repositories.ExecSpecification

class SetUserStatus(private val id: Int?, private val state: StateType) : ExecSpecification {
    override val sql: String
        get() = "update states set state = :state where user_id = :user_id"

    override val sqlParameterSource: Map<String, *>
        get() =
            MapSqlParameterSource()
                .addValue("state", state.state)
                .addValue("user_id", id)
                .values
}
