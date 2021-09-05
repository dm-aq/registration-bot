package ru.registration.bot.repositories.specifications

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import ru.registration.bot.engine.commands.flow.StateType.REQUEST_READY
import ru.registration.bot.engine.commands.flow.states.Sex
import ru.registration.bot.engine.configuration.annotation.SpringBootIT
import ru.registration.bot.repositories.RequestRepository
import java.util.UUID
import kotlin.random.Random

@SpringBootIT
class UserRequestIT {

    @Autowired
    lateinit var jdbcTemplate: NamedParameterJdbcTemplate

    @Autowired
    lateinit var requestRepository: RequestRepository

    @Test
    fun `selecting user request`() {

        // arrange
        val userId = Random.nextInt()
        val tgLogin = UUID.randomUUID().toString()
        val fullName = "John Smith"
        val email = "mail@mail.ru"
        val phone = "98881234567"
        val roomType = 1
        val danceStyle = "salsa"
        val neighbors = "nobody"
        val state = REQUEST_READY
        jdbcTemplate.update(
            "insert into requests(" +
                "id, user_id, telegram_login, full_name, email, phone, " +
                "sex, room_type, dance_type, neighbors, state, insstmp, updstmp) " +
                "values(nextval('requests_pk_seq'), :user_id, " +
                ":telegram_login, :full_name, :email, :phone, :sex, :room_type, :dance_type, :neighbors, " +
                ":state, current_timestamp, current_timestamp) ",
            MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("telegram_login", tgLogin)
                .addValue("state", state.name)
                .addValue("full_name", fullName)
                .addValue("email", email)
                .addValue("phone", phone)
                .addValue("sex", Sex.MALE.value)
                .addValue("room_type", roomType)
                .addValue("dance_type", danceStyle)
                .addValue("neighbors", neighbors)
                .values
        )

        // act
        val res = requestRepository.query(UserRequest(userId, state))

        assertEquals(1, res.size)

        val req = res.first()
        assertEquals(fullName, req.fullName)
        assertEquals(email, req.email)
        assertEquals(phone, req.phone)
        assertEquals(Sex.MALE.value, req.sex)
        assertEquals(roomType, req.roomType)
        assertEquals(danceStyle, req.danceType)
        assertEquals(neighbors, req.neighbors)
        assertEquals(tgLogin, req.telegramLogin)
    }
}
