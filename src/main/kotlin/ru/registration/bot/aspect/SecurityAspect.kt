package ru.registration.bot.aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender

@Aspect
@Component
class SecurityAspect(
    @Value("\${allowed-user-id:}")  private val allowedUserId: String
) {

    @Around("@annotation(Secured)")
    fun checkUser(joinPoint: ProceedingJoinPoint): Any? {
        val message: Message = joinPoint.args[1] as Message
        val senderId = message.from.id

        if(allowedUserId.isNotEmpty() && allowedUserId == senderId.toString()) {
            return joinPoint.proceed()
        }

        sendReject(joinPoint.args[0] as AbsSender, message.chat)

        return null
    }

    private fun sendReject(absSender: AbsSender?, chat: Chat?){
        absSender?.execute(SendMessage(chat?.id, "Извините, я вас не знаю."))
    }
}
