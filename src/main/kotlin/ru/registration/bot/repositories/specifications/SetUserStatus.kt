package ru.registration.bot.repositories.specifications

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import ru.registration.bot.engine.commands.flow.StateType
import ru.registration.bot.repositories.ExecSpecification

class SetUserStatus(
    private val id: Int?,
    private val oldState: StateType,
    private val newState: StateType
) : ExecSpecification {
    override val sql: String
        get() = "update requests set state = :new_state, updstmp = current_timestamp " +
                "where user_id = :user_id and state = :previous_state"

    override val sqlParameterSource: Map<String, *>
        get() =
            MapSqlParameterSource()
                .addValue("new_state", newState.name)
                .addValue("user_id", id)
                .addValue("previous_state", oldState.name)
                .values
}
