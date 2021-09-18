package ru.registration.bot.repositories

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class BotRepository(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) {
    fun execute(specification: ExecSpecification) =
        namedParameterJdbcTemplate.update(specification.sql, specification.sqlParameterSource)

    fun <T> query(specification: QuerySpecification<T>): List<T> =
        namedParameterJdbcTemplate.query(specification.sql, specification.sqlParameterSource, specification.rowMapper)
}
