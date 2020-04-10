package ru.registration.bot.repositories.specifications

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import ru.registration.bot.engine.commands.flow.StateType
import ru.registration.bot.repositories.ExecSpecification

class SetUserStatus(private val id: Int?, private val state: StateType) : ExecSpecification {
    override val sql: String
        get() = "update requests set state = :state where user_id = :user_id and state <> :finishedState"

    override val sqlParameterSource: Map<String, *>
        get() =
            MapSqlParameterSource()
                .addValue("state", state.state)
                .addValue("user_id", id)
                .addValue("finishedState", StateType.EXPORTED.state)
                .values
}
