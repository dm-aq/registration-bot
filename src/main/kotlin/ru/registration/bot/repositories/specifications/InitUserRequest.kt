package ru.registration.bot.repositories.specifications

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import ru.registration.bot.repositories.ExecSpecification

class InitUserRequest(private val id: Int?) : ExecSpecification {
    override val sql: String
        get() = "insert into requests(id, user_id) " +
                "values(nextval('requests_pk_seq'), :user_id) "
    override val sqlParameterSource: Map<String, *>
        get() = MapSqlParameterSource("user_id", id).values

}
