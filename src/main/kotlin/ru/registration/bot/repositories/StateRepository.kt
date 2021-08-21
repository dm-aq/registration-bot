package ru.registration.bot.repositories

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.registration.bot.engine.commands.flow.StateType

@Repository
class StateRepository(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) {

    fun execute(specification: ExecSpecification) =
        namedParameterJdbcTemplate.update(specification.sql, specification.sqlParameterSource)

    fun query(specification: QuerySpecification<StateType>): List<StateType> =
        namedParameterJdbcTemplate.query(specification.sql, specification.sqlParameterSource, specification.rowMapper)
}
