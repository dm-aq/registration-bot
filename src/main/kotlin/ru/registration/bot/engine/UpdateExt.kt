package ru.registration.bot.engine

import org.telegram.telegrambots.meta.api.objects.Update

val Update.text: String?
    get() =
        if (this.hasCallbackQuery()) {
            this.callbackQuery?.data
        } else {
            this.message?.text
        }

val Update.chat
    get() =
        if (this.hasCallbackQuery()) {
            this.callbackQuery?.message?.chat
        } else {
            this.message?.chat
        }

val Update.user
    get() =
        if (this.hasCallbackQuery()) {
            this.callbackQuery?.from
        } else {
            this.message?.from
        }
