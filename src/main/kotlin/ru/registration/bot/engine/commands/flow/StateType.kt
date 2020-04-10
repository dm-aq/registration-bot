package ru.registration.bot.engine.commands.flow

enum class StateType(val state: Int) {
    START_STATE(0),
    PHONE_STATE(11),
    FULL_NAME_STATE(12),
    SEX_STATE(13),
    ROOM_STATE(14),
    DANCESTYLE_STATE(15),
    NEIGHBORS_STATE(16),
    REQUEST_READY(2),
    EXPORTED(21);

    companion object {
        private val values = values();
        fun getByValue(value: Int) = values.firstOrNull { it.state == value }
    }
}