package ru.registration.bot.repositories.specifications

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.BadSqlGrammarException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import ru.registration.bot.engine.commands.flow.StateType.EXPORTED
import ru.registration.bot.engine.commands.flow.StateType.START_STATE
import ru.registration.bot.engine.configuration.annotation.SpringBootIT
import ru.registration.bot.repositories.BotRepository
import java.util.UUID
import kotlin.random.Random

@SpringBootIT
class UpdateRequestFieldIT {

    @Autowired
    lateinit var jdbcTemplate: NamedParameterJdbcTemplate

    @Autowired
    lateinit var requestRepository: BotRepository

    @Test
    fun `updating existing request field`() {
        // arrange
        val userId = Random.nextInt()
        val tgLogin = UUID.randomUUID().toString()
        val state = START_STATE
        jdbcTemplate.update(
            "insert into requests(id, user_id, state, telegram_login, insstmp, updstmp) " +
                "values(nextval('requests_pk_seq'), :user_id, :state, " +
                ":telegram_login, current_timestamp, current_timestamp) ",
            MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("state", state.name)
                .addValue("telegram_login", tgLogin)
                .values
        )

        // act
        val email = "mail@mail.ru"
        requestRepository.execute(UpdateRequestField(userId, Pair("email", email)))

        // assert
        val req = jdbcTemplate.queryForObject(
            "select 1 from requests where user_id = :userId and email = :email ",
            MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("email", email)
                .values,
            Boolean::class.java
        )

        assertTrue(req!!)
    }

    @Test
    fun `updating existing request field in final state`() {
        // arrange
        val userId = Random.nextInt()
        val tgLogin = UUID.randomUUID().toString()
        val state = EXPORTED
        jdbcTemplate.update(
            "insert into requests(id, user_id, state, telegram_login, insstmp, updstmp) " +
                "values(nextval('requests_pk_seq'), :user_id, :state, " +
                ":telegram_login, current_timestamp, current_timestamp) ",
            MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("state", state.name)
                .addValue("telegram_login", tgLogin)
                .values
        )

        // act
        val email = "mail@mail.ru"
        requestRepository.execute(UpdateRequestField(userId, Pair("email", email)))

        // assert
        val req = jdbcTemplate.queryForList(
            "select 1 from requests where user_id = :userId and email = :email ",
            MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("email", email)
                .values,
            Int::class.java
        )

        assertTrue(req.isEmpty())
    }

    @Test
    fun `updating not existing request field`() {
        // arrange
        val userId = Random.nextInt()
        val tgLogin = UUID.randomUUID().toString()
        val state = START_STATE
        jdbcTemplate.update(
            "insert into requests(id, user_id, state, telegram_login, insstmp, updstmp) " +
                "values(nextval('requests_pk_seq'), :user_id, :state, " +
                ":telegram_login, current_timestamp, current_timestamp) ",
            MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("state", state.name)
                .addValue("telegram_login", tgLogin)
                .values
        )

        // act & assert
        assertThrows<BadSqlGrammarException> {
            requestRepository.execute(UpdateRequestField(userId, Pair("wrong_field_name", "mail@mail.ru")))
        }
    }
}
