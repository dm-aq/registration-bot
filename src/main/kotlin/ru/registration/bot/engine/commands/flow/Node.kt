package ru.registration.bot.engine.commands.flow

interface Node<T> {
    val prev: T?
    val next: T?
}
