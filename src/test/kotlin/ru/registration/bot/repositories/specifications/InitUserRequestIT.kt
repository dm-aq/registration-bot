package ru.registration.bot.repositories.specifications

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import ru.registration.bot.engine.commands.flow.StateType.PHONE_STATE
import ru.registration.bot.engine.configuration.annotation.SpringBootIT
import ru.registration.bot.repositories.BotRepository
import kotlin.random.Random

@SpringBootIT
class InitUserRequestIT {

    @Autowired
    lateinit var jdbcTemplate: NamedParameterJdbcTemplate

    @Autowired
    lateinit var requestRepo: BotRepository

    @Test
    fun `init user request`() {

        // arrange
        val userId = Random.nextInt()
        val state = PHONE_STATE

        // act
        requestRepo.execute(InitUserRequest(userId, state))

        // assert
        val req = jdbcTemplate.queryForObject(
            "select 1 from requests where user_id = :userId and state = :state ",
            MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("state", state.name)
                .values,
            Boolean::class.java
        )

        assertTrue(req!!)
    }
}
