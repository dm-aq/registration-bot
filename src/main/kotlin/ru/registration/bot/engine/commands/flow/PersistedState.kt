package ru.registration.bot.engine.commands.flow

interface PersistedState : State {
    val type: StateType
}
