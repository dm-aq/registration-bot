package ru.registration.bot.engine.commands.flow

import org.telegram.telegrambots.meta.api.objects.Update

interface State {
    fun ask()
    fun handle(update: Update?)
}
