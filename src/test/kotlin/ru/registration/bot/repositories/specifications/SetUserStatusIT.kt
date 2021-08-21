package ru.registration.bot.repositories.specifications

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import ru.registration.bot.engine.commands.flow.StateType.NEIGHBORS_STATE
import ru.registration.bot.engine.commands.flow.StateType.PHONE_STATE
import ru.registration.bot.engine.configuration.annotation.SpringBootIT
import ru.registration.bot.repositories.StateRepository
import java.util.UUID
import kotlin.random.Random

@SpringBootIT
class SetUserStatusIT {

    @Autowired
    lateinit var jdbcTemplate: NamedParameterJdbcTemplate

    @Autowired
    lateinit var stateRepo: StateRepository

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
        stateRepo.execute(SetUserStatus(userId, PHONE_STATE, NEIGHBORS_STATE))

        // assert
        val req = jdbcTemplate.queryForObject(
            "select 1 from requests where user_id = :userId and state = :state ",
            MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("state", state.name)
                .values,
            Boolean::class.java
        )

        Assertions.assertTrue(req!!)
    }
}
