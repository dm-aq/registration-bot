package ru.registration.bot.engine

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.registration.bot.engine.commands.flow.State
import ru.registration.bot.engine.commands.flow.StateType.MAIL_STATE
import ru.registration.bot.engine.commands.flow.StateType.START_STATE
import ru.registration.bot.repositories.StateRepository

class CommonFactoryTest {

    lateinit var stateRepo: StateRepository
    lateinit var states: Map<String, State>
    lateinit var commonFactory: CommonFactory

    @BeforeEach
    fun setUp() {
        stateRepo = mock()
        states = mock()
        commonFactory = CommonFactory(stateRepo, states)
    }

    @Test
    fun `new customer`() {
        // arrange
        given { stateRepo.query(any()) }.willReturn(emptyList())

        // act
        val res = commonFactory.currentUserStateType(123)

        // assert
        assertEquals(START_STATE, res)
    }

    @Test
    fun `existing customer`() {
        // arrange
        given { stateRepo.query(any()) }.willReturn(listOf(MAIL_STATE))

        // act
        val res = commonFactory.currentUserStateType(123)

        // assert
        assertEquals(MAIL_STATE, res)
    }
}
