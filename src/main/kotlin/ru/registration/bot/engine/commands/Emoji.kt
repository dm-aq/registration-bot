package ru.registration.bot.engine.commands

enum class Emoji(
    private val code: Int
) {
    POINT_FINGER_RIGHT(0x1F449),
    POINT_FINGER_LEFT(0x1F448);

    override fun toString() =
        String(Character.toChars(code))
}
