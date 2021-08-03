package ru.registration.bot

import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.extensions.bots.commandbot.commands.CommandRegistry
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand
import org.telegram.telegrambots.extensions.bots.commandbot.commands.ICommandRegistry
import org.telegram.telegrambots.meta.ApiContext
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender
import java.util.function.BiConsumer

abstract class BaseTelegramLongPollingBot(
    private val botName: String,
    private val token: String,
    allowCommandsWithUserName: Boolean = true
) : TelegramLongPollingBot(ApiContext.getInstance(DefaultBotOptions::class.java)), ICommandRegistry {

    private val commandRegistry = CommandRegistry(allowCommandsWithUserName, botName)

    override fun getBotUsername() = botName

    override fun getBotToken() = token

    override fun onUpdateReceived(update: Update?) {
        if (update!!.hasMessage()) {
            val message = update.message
            if (message.isCommand && !filter(message)) {
                if (!commandRegistry.executeCommand(this, message)) {
                    processInvalidCommandUpdate(update)
                }
                return
            }
        }
        processNonCommandUpdate(update)
    }

    protected open fun filter(message: Message?): Boolean {
        return false
    }

    protected open fun processInvalidCommandUpdate(update: Update?) {
        processNonCommandUpdate(update)
    }

    abstract fun processNonCommandUpdate(update: Update?)

    override fun registerDefaultAction(defaultConsumer: BiConsumer<AbsSender, Message>?) =
        commandRegistry.registerDefaultAction(defaultConsumer)

    override fun register(botCommand: IBotCommand?): Boolean = commandRegistry.register(botCommand)

    override fun registerAll(vararg botCommands: IBotCommand?): MutableMap<IBotCommand, Boolean> =
        commandRegistry.registerAll(*botCommands)

    override fun deregister(botCommand: IBotCommand?): Boolean = commandRegistry.deregister(botCommand)

    override fun deregisterAll(vararg botCommands: IBotCommand?): MutableMap<IBotCommand, Boolean> =
        commandRegistry.deregisterAll(*botCommands)

    override fun getRegisteredCommands(): MutableCollection<IBotCommand> =
        commandRegistry.registeredCommands

    override fun getRegisteredCommand(commandIdentifier: String?): IBotCommand =
        commandRegistry.getRegisteredCommand(commandIdentifier)
}
