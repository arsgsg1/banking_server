package com.numble.team2.repository

import com.numble.team2.domain.MemberEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MemberRepository: JpaRepository<MemberEntity, Long> {
    fun findByEmail(email: String): Optional<MemberEntity>
}