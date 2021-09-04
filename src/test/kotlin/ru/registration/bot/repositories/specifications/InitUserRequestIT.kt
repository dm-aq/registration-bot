package ru.registration.bot.repositories.specifications

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.telegram.telegrambots.meta.api.objects.User
import ru.registration.bot.engine.commands.flow.StateType.PHONE_STATE
import ru.registration.bot.engine.configuration.annotation.SpringBootIT
import ru.registration.bot.repositories.RequestRepository
import java.util.UUID
import kotlin.random.Random

@SpringBootIT
class InitUserRequestIT {

    @Autowired
    lateinit var jdbcTemplate: NamedParameterJdbcTemplate

    @Autowired
    lateinit var requestRepo: RequestRepository

    @Test
    fun `init user request`() {

        // arrange
        val userId = Random.nextInt()
        val tgLogin = UUID.randomUUID().toString()
        val state = PHONE_STATE
        val user: User = mock {
            on { id } doReturn userId
            on { userName } doReturn tgLogin
        }

        // act
        requestRepo.execute(InitUserRequest(user, state))

        // assert
        val req = jdbcTemplate.queryForObject(
            "select 1 from requests where user_id = :userId and telegram_login = :tgLogin ",
            MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("tgLogin", tgLogin)
                .values,
            Boolean::class.java
        )

        assertTrue(req!!)
    }
}
