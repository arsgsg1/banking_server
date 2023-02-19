package com.numble.team2.service

import com.numble.team2.domain.MemberEntity
import com.numble.team2.dto.SignUpRequest
import com.numble.team2.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {
    @Transactional(readOnly = true)
    fun checkNotDuplicateEmail(email: String): Boolean {
        return !memberRepository.findByEmail(email).isPresent
    }

    @Transactional
    fun createMember(dto: SignUpRequest): Long {
        require(checkNotDuplicateEmail(dto.email)) { "이미 존재하는 이메일입니다." }
        val memberEntity = MemberEntity(email = dto.email, password = dto.password)
        return memberRepository.save(memberEntity).id
    }
}