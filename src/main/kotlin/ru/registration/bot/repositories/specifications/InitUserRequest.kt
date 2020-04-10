package ru.registration.bot.repositories.specifications

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import ru.registration.bot.engine.commands.flow.StateType
import ru.registration.bot.repositories.ExecSpecification

class InitUserRequest(
    private val id: Int?,
    private val state: StateType
) : ExecSpecification {
    override val sql: String
        get() = "insert into requests(id, user_id, state, insstmp, updstmp) " +
                "values(nextval('requests_pk_seq'), :user_id, :state, current_timestamp, current_timestamp) "
    override val sqlParameterSource: Map<String, *>
        get() = MapSqlParameterSource()
            .addValue("user_id", id)
            .addValue("state", state.state)
            .values

}
