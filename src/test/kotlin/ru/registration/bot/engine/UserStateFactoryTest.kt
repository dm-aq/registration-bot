package ru.registration.bot.engine

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.registration.bot.engine.commands.flow.State
import ru.registration.bot.engine.commands.flow.StateType
import ru.registration.bot.engine.commands.flow.StateType.MAIL_STATE
import ru.registration.bot.engine.commands.flow.StateType.START_STATE
import ru.registration.bot.repositories.BotRepository

class UserStateFactoryTest {

    lateinit var stateRepo: BotRepository
    lateinit var states: Map<String, State>
    lateinit var userStateFactory: UserStateFactory

    @BeforeEach
    fun setUp() {
        stateRepo = mock()
        states = mock()
        userStateFactory = UserStateFactory(stateRepo, states)
    }

    @Test
    fun `new customer`() {
        // arrange
        given { stateRepo.query<StateType>(any()) }.willReturn(emptyList())

        // act
        val res = userStateFactory.currentUserStateType(123)

        // assert
        assertEquals(START_STATE, res)
    }

    @Test
    fun `existing customer`() {
        // arrange
        given { stateRepo.query<StateType>(any()) }.willReturn(listOf(MAIL_STATE))

        // act
        val res = userStateFactory.currentUserStateType(123)

        // assert
        assertEquals(MAIL_STATE, res)
    }
}
