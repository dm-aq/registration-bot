package ru.registration.bot.repositories

interface ExecSpecification {
    val sql: String

    val sqlParameterSource: Map<String, *>
}
