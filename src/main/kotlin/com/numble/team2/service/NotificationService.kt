package com.numble.team2.service

import com.numble.team2.domain.NotificationHistory
import com.numble.team2.dto.NotificationEvent
import com.numble.team2.exception.MemberNotFoundException
import com.numble.team2.repository.MemberRepository
import com.numble.team2.repository.NotificationHistoryRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class NotificationService(
    private val notificationHistoryRepository: NotificationHistoryRepository,
    private val memberRepository: MemberRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    suspend fun createNotificationHistory(event: NotificationEvent): Long {
        val fromMember = memberRepository.findByIdOrNull(event.fromMemberId) ?: throw MemberNotFoundException(event.fromMemberId)
        val toMember = memberRepository.findByIdOrNull(event.toMemberId) ?: throw MemberNotFoundException(event.toMemberId)
        return notificationHistoryRepository.save(NotificationHistory(from = fromMember, to = toMember, money = event.money)).id
    }

    suspend fun sendNotification(event: NotificationEvent) {
        // 여기에 외부 알람 서버를 호출하는 코드가 들어갑니다
        logger.info("${event.fromMemberId}번 유저가 ${event.toMemberId}번 유저에게 ${event.money}원을 송금했습니다.")
    }
}