package ru.registration.bot.engine.configuration.annotation

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import ru.registration.bot.engine.configuration.ITContextInitializer

@ContextConfiguration(
    initializers = [ITContextInitializer::class]
)
@SpringBootTest
@ActiveProfiles("test")
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class SpringBootIT
