package ru.registration.bot.repositories.specifications

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import ru.registration.bot.engine.commands.flow.StateType
import ru.registration.bot.repositories.ExecSpecification

class SetUserStatusByReqId(
    private val requestId: Int?,
    private val newState: StateType
) : ExecSpecification {
    override val sql: String
        get() = "update requests set state = :new_state, updstmp = current_timestamp " +
                "where id = :req_id"
    override val sqlParameterSource: Map<String, *>
        get() =
            MapSqlParameterSource()
                .addValue("state", newState.name)
                .addValue("req_id", requestId)
                .addValue("new_state", newState.name)
                .values
}
