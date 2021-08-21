package ru.registration.bot.postgresql

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import ru.registration.bot.engine.configuration.annotation.SpringBootIT
import javax.sql.DataSource

@SpringBootIT
class PostgreSqlContainerIT {

    @Autowired
    @Qualifier("dataSource")
    lateinit var datasource: DataSource

    @Test
    fun testSelectQueryReturnsResult() {

        // arrange
        val connection = datasource.connection

        // act
        val result = connection.createStatement().executeQuery("SELECT 1")
        result.next()

        // assert
        assertEquals(1, result.getInt(1))
    }
}
