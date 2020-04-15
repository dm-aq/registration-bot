package ru.registration.bot.repositories.specifications

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.telegram.telegrambots.meta.api.objects.User
import ru.registration.bot.engine.commands.flow.StateType
import ru.registration.bot.repositories.ExecSpecification

class InitUserRequest(
    private val user: User?,
    private val state: StateType
) : ExecSpecification {
    override val sql: String
        get() = "insert into requests(id, user_id, telegram_login, state, insstmp, updstmp) " +
            "values(nextval('requests_pk_seq'), :user_id, " +
            ":telegram_login, :state, current_timestamp, current_timestamp) "
    override val sqlParameterSource: Map<String, *>
        get() = MapSqlParameterSource()
            .addValue("user_id", user?.id)
            .addValue("telegram_login", user?.userName)
            .addValue("state", state.name)
            .values

}
