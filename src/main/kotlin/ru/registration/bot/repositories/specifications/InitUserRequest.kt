package ru.registration.bot.repositories.specifications

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import ru.registration.bot.engine.commands.flow.StateType
import ru.registration.bot.repositories.ExecSpecification

class InitUserRequest(
    private val userId: Int,
    private val state: StateType
) : ExecSpecification {
    override val sql: String
        get() = "insert into requests(id, user_id, telegram_login, state, insstmp, updstmp) " +
            "values(nextval('requests_pk_seq'), :user_id, " +
            ":telegram_login, :state, current_timestamp, current_timestamp) "
    override val sqlParameterSource: Map<String, *>
        get() = MapSqlParameterSource()
            .addValue("user_id", userId)
            .addValue("telegram_login", null) // todo how to get tg_login ?
            .addValue("state", state.name)
            .values
}
