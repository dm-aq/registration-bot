package ru.registration.bot.repositories.specifications

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import ru.registration.bot.repositories.ExecSpecification

class UpdateRequestField(
    private val userId: Int?,
    private val pair: Pair<String, Any>
): ExecSpecification {
    override val sql: String
        get() = "update requests set ${pair.first} = :${pair.first} where user_id = :user_id "
    override val sqlParameterSource: Map<String, *>
        get() = MapSqlParameterSource().addValue("${pair.first}", pair.second).addValue("user_id", userId).values
}