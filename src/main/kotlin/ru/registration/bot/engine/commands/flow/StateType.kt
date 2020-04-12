package ru.registration.bot.engine.commands.flow

enum class StateType {
    START_STATE,
    PHONE_STATE,
    FULL_NAME_STATE,
    MAIL_STATE,
    SEX_STATE,
    ROOM_STATE,
    DANCESTYLE_STATE,
    NEIGHBORS_STATE,
    REQUEST_READY,
    REQUEST_APPROVED,
    EXPORTED;
}