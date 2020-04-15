package ru.registration.bot.engine.commands.flow

interface State {
    fun ask()
    fun handle(text: String?)
}
