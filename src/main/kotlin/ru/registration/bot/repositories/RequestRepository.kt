package ru.registration.bot.repositories

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.registration.bot.engine.commands.Request

@Repository
open class RequestRepository(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) {
    fun execute(specification: ExecSpecification) =
        namedParameterJdbcTemplate.update(specification.sql, specification.sqlParameterSource)

    fun query(specification: QuerySpecification<Request>): List<Request> =
        namedParameterJdbcTemplate.query(specification.sql, specification.sqlParameterSource, specification.rowMapper)
}