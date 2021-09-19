package ru.registration.bot.engine.testcontainers

import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

object PostgreSqlContainerConfig {

    private val postgresSqlContainer = KPostgreSqlContainer(
        DockerImageName.parse("postgres:12")
            .asCompatibleSubstituteFor("postgres")
    )
        .withDatabaseName("carambola")
        .withUsername("postgres")
        .withPassword("postgres")

    val jdbcUrl: String by lazy {
        postgresSqlContainer.jdbcUrl
    }

    fun startContainer() = postgresSqlContainer.start()

    private class KPostgreSqlContainer(
        dockerImageName: DockerImageName
    ) : PostgreSQLContainer<KPostgreSqlContainer>(dockerImageName)
}
