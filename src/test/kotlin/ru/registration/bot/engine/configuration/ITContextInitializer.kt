package ru.registration.bot.engine.configuration

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import ru.registration.bot.engine.testcontainers.PostgreSqlContainerConfig

class ITContextInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        initDatasource(applicationContext)
    }

    private fun initDatasource(applicationContext: ConfigurableApplicationContext) {
        PostgreSqlContainerConfig.startContainer()

        TestPropertyValues.of(
            "spring.datasource.url=" + PostgreSqlContainerConfig.jdbcUrl
        )
            .applyTo(applicationContext.environment)
    }
}
