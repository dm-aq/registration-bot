package ru.registration.bot.engine.commands.flow

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.registration.bot.engine.CommonFactory

class PaymentMessage(
    private val id: Long?,
    private val absSender: AbsSender?,
    private val commonFactory: CommonFactory,
    private val roomType: Int?
) {

    fun send() {

        val message = """
            Стоимость участия: ${calculateCost()}
            Cсылка для оплаты: ${link()}
        """.trimIndent()
        absSender?.execute(SendMessage(id, message))
    }

    private fun calculateCost(): String? {
        return commonFactory.roomCategoryProperties.prices[roomType]
    }

    private fun link(): String {
        return "http://ya.ru"
    }
}
