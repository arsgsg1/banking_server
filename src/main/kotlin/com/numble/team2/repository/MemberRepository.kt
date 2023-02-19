package com.numble.team2.repository

import com.numble.team2.domain.MemberEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MemberRepository: JpaRepository<MemberEntity, Long> {
    fun findByEmail(email: String): Optional<MemberEntity>

    @Query("SELECT m FROM MemberEntity m LEFT JOIN FETCH m.friends f WHERE m.id = :memberId")
    fun findMemberWithAllFriends(memberId: Long): Optional<MemberEntity>
}