package ru.registration.bot.engine.commands

enum class Emoji(
    private val code: Int
) {
    POINT_FINGER_RIGHT(0x1F449),
    POINT_FINGER_LEFT(0x1F448),
    DANCING_MAN(0x1F57A),
    DANCING_WOMAN(0x1F483);

    override fun toString() =
        String(Character.toChars(code))
}
