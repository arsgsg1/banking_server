package com.numble.team2.global

import com.numble.team2.dto.NotificationEvent
import com.numble.team2.service.NotificationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener

@Component
class NotificationEventListener(
    private val notificationService: NotificationService
) {
    @TransactionalEventListener
    fun notificationEventHandler(notificationEvent: NotificationEvent) {
        CoroutineScope(Dispatchers.Default).launch {
            async { notificationService.createNotificationHistory(notificationEvent) }
            async { notificationService.sendNotification(notificationEvent) }
        }
    }
}