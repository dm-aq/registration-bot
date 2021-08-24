package ru.registration.bot.engine

import org.telegram.telegrambots.meta.api.objects.Update

val Update.text: String?
    get() =
        if (this.hasCallbackQuery()) {
            this.callbackQuery?.data
        } else {
            this.message?.text
        }

val Update.chatId : Long
    get() =
        if (this.hasCallbackQuery()) {
            this.callbackQuery?.message?.chat?.id ?: throw IllegalStateException("There is no chatId")
        } else {
            this.message?.chat?.id ?: throw IllegalStateException("There is no chatId")
        }

val Update.chat
    get() =
        if (this.hasCallbackQuery()) {
            this.callbackQuery?.message?.chat
        } else {
            this.message?.chat
        }

val Update.userId : Int
    get() =
        if (this.hasCallbackQuery()) {
            this.callbackQuery?.from?.id ?: throw IllegalStateException("There is no userId")
        } else {
            this.message?.from?.id ?: throw IllegalStateException("There is no userId")
        }

val Update.user
    get() =
        if (this.hasCallbackQuery()) {
            this.callbackQuery?.from
        } else {
            this.message?.from
        }
