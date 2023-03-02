package com.numble.team2.repository

import com.numble.team2.domain.NotificationHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NotificationHistoryRepository : JpaRepository<NotificationHistory, Long>