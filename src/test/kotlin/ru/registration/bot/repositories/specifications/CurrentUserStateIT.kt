package ru.registration.bot.repositories.specifications

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import ru.registration.bot.engine.commands.flow.StateType.EXPORTED
import ru.registration.bot.engine.commands.flow.StateType.PHONE_STATE
import ru.registration.bot.engine.configuration.annotation.SpringBootIT
import ru.registration.bot.repositories.BotRepository
import java.util.UUID
import kotlin.random.Random

@SpringBootIT
class CurrentUserStateIT {

    @Autowired
    lateinit var jdbcTemplate: NamedParameterJdbcTemplate

    @Autowired
    lateinit var stateRepo: BotRepository

    @Test
    fun `user in draft status`() {

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
        val res = stateRepo.query(CurrentUserState(userId))

        // assert
        assertEquals(1, res.size)
        assertEquals(state, res.first())
    }

    @Test
    fun `user in non draft status`() {
        // arrange
        val userId = Random.nextInt()
        val tgLogin = UUID.randomUUID().toString()
        val state = EXPORTED
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
        val res = stateRepo.query(CurrentUserState(userId))

        // assert
        assertTrue(res.isEmpty())
    }

    @Test
    fun `no user status`() {
        val res = stateRepo.query(CurrentUserState(Random.nextInt()))

        assertTrue(res.isEmpty())
    }
}
