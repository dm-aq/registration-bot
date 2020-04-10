package ru.registration.bot.repositories.specifications

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import ru.registration.bot.engine.commands.flow.StateType
import ru.registration.bot.repositories.ExecSpecification

class UpdateRequestField(
    private val userId: Int?,
    private val pair: Pair<String, Any>
): ExecSpecification {
    override val sql: String
        get() = "update requests set ${pair.first} = :${pair.first}, updstmp = current_timestamp " +
                "where user_id = :user_id and state in (:draft_states)"
    override val sqlParameterSource: Map<String, *>
        get() = MapSqlParameterSource()
            .addValue("${pair.first}", pair.second)
            .addValue("user_id", userId)
            .addValue("draft_states",
                listOf(
                    StateType.START_STATE.state,
                    StateType.FULL_NAME_STATE.state,
                    StateType.PHONE_STATE.state,
                    StateType.SEX_STATE.state,
                    StateType.ROOM_STATE.state,
                    StateType.DANCESTYLE_STATE.state,
                    StateType.NEIGHBORS_STATE.state
                )
            ).values
}