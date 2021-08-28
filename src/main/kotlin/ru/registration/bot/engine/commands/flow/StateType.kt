package ru.registration.bot.engine.commands.flow

enum class StateType(
    val beanName: String
) {
    START_STATE("startState"),
    PHONE_STATE("phoneNumberState"),
    FULL_NAME_STATE("fullNameState"),
    MAIL_STATE("mailState"),
    SEX_STATE("sexState"),
    ROOM_STATE("roomCategoryState"),
    DANCESTYLE_STATE("danceStyleState"),
    NEIGHBORS_STATE("neighborsState"),
    REQUEST_READY("draftState"),
    REQUEST_APPROVED("exportState"),
    EXPORTED("endState"); // todo add final end state
}
