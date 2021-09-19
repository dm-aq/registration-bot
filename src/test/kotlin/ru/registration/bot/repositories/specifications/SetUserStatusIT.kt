package ru.registration.bot.repositories.specifications

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import ru.registration.bot.engine.commands.flow.StateType.EXPORTED
import ru.registration.bot.engine.commands.flow.StateType.NEIGHBORS_STATE
import ru.registration.bot.engine.commands.flow.StateType.PHONE_STATE
import ru.registration.bot.engine.commands.flow.StateType.REQUEST_APPROVED
import ru.registration.bot.engine.configuration.annotation.SpringBootIT
import ru.registration.bot.repositories.BotRepository
import java.util.UUID
import kotlin.random.Random

@SpringBootIT
class SetUserStatusIT {

    @Autowired
    lateinit var jdbcTemplate: NamedParameterJdbcTemplate

    @Autowired
    lateinit var stateRepo: BotRepository

    @Test
    fun `setting user status`() {

        // arrange
        val userId = Random.nextInt()
        val tgLogin = UUID.randomUUID().toString()
        val state = PHONE_STATE
        jdbcTemplate.update(
            "insert into requests(id, user_id, telegram_login, state, insstmp, updstmp) " +
                "values(nextval('requests_pk_seq'), :user_id, " +
                ":telegram_login, :state, current_timestamp, current_timestamp) ",
            MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("telegram_login", tgLogin)
                .addValue("state", state.name)
                .values
        )

        // act
        val newState = NEIGHBORS_STATE
        stateRepo.execute(SetUserStatus(userId, newState))

        // assert
        val req = jdbcTemplate.queryForObject(
            "select 1 from requests where user_id = :userId and state = :state ",
            MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("state", newState.name)
                .values,
            Boolean::class.java
        )

        assertTrue(req!!)
    }

    @Test
    fun `setting user status with existing request`() {
        // arrange
        val userId = Random.nextInt()
        val tgLogin = UUID.randomUUID().toString()

        jdbcTemplate.update(
            "insert into requests(id, user_id, telegram_login, state, insstmp, updstmp) " +
                "values(nextval('requests_pk_seq'), :user_id, " +
                ":telegram_login, :state, current_timestamp, current_timestamp) ",
            MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("telegram_login", tgLogin)
                .addValue("state", REQUEST_APPROVED.name)
                .values
        )

        jdbcTemplate.update(
            "insert into requests(id, user_id, telegram_login, state, insstmp, updstmp) " +
                "values(nextval('requests_pk_seq'), :user_id, " +
                ":telegram_login, :state, current_timestamp, current_timestamp) ",
            MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("telegram_login", tgLogin)
                .addValue("state", EXPORTED.name)
                .values
        )

        val state = PHONE_STATE
        jdbcTemplate.update(
            "insert into requests(id, user_id, telegram_login, state, insstmp, updstmp) " +
                "values(nextval('requests_pk_seq'), :user_id, " +
                ":telegram_login, :state, current_timestamp, current_timestamp) ",
            MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("telegram_login", tgLogin)
                .addValue("state", state.name)
                .values
        )

        // act
        val newState = NEIGHBORS_STATE
        stateRepo.execute(SetUserStatus(userId, newState))

        // assert
        val req = jdbcTemplate.queryForObject(
            "select count(*) from requests where user_id = :userId and state = :state ",
            MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("state", newState.name)
                .values,
            Int::class.java
        )

        assertEquals(1, req!!)
    }
}
