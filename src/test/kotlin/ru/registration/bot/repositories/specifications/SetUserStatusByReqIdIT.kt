package ru.registration.bot.repositories.specifications

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import ru.registration.bot.engine.commands.flow.StateType.EXPORTED
import ru.registration.bot.engine.commands.flow.StateType.REQUEST_APPROVED
import ru.registration.bot.engine.configuration.annotation.SpringBootIT
import ru.registration.bot.repositories.StateRepository
import java.util.UUID
import kotlin.random.Random

@SpringBootIT
class SetUserStatusByReqIdIT {

    @Autowired
    lateinit var jdbcTemplate: NamedParameterJdbcTemplate

    @Autowired
    lateinit var stateRepo: StateRepository

    @Test
    fun `setting user status by request id`() {

        // arrange
        val reqId = Random.nextInt(1000, 10_000)
        val userId = Random.nextInt()
        val tgLogin = UUID.randomUUID().toString()
        val state = REQUEST_APPROVED
        jdbcTemplate.update(
            "insert into requests(id, user_id, telegram_login, state, insstmp, updstmp) " +
                "values(:req_id, :user_id, :telegram_login, :state, current_timestamp, current_timestamp) ",
            MapSqlParameterSource()
                .addValue("req_id", reqId)
                .addValue("user_id", userId)
                .addValue("telegram_login", tgLogin)
                .addValue("state", state.name)
                .values
        )

        // act
        val newState = EXPORTED
        stateRepo.execute(SetUserStatusByReqId(reqId, newState))

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
}
