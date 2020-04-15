package ru.registration.bot.repositories

import org.springframework.jdbc.core.RowMapper

interface QuerySpecification<T> {
    val sql: String
    val sqlParameterSource: Map<String?, *>
    val rowMapper: RowMapper<T>
}
