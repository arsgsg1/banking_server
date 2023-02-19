package com.numble.team2.service

import com.numble.team2.domain.MemberEntity
import com.numble.team2.domain.MemberFriendEntity
import com.numble.team2.dto.MemberFriendRequest
import com.numble.team2.dto.SignUpRequest
import com.numble.team2.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalArgumentException
import java.util.stream.Collectors

@Service
class MemberService(
    private val memberRepository: MemberRepository,
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

    @Transactional
    fun createFriend(memberId: Long, dto: MemberFriendRequest) {
        val memberEntity = memberRepository.findMemberWithAllFriends(memberId)
            .orElseThrow { throw IllegalArgumentException("해당 ID의 유저가 없습니다. [${memberId}]") }
        memberEntity.friends.firstOrNull { entity -> entity.friendId == dto.friendId } ?: run {
            val memberFriendEntity =
                MemberFriendEntity(memberId = memberId, friendId = dto.friendId)
            memberEntity.friends.add(memberFriendEntity)
        }
    }

    @Transactional(readOnly = true)
    fun getAllFriends(memberId: Long): List<Long> {
        val memberEntity = memberRepository.findMemberWithAllFriends(memberId)
            .orElseThrow { throw IllegalArgumentException("해당 ID의 유저가 없습니다. [${memberId}]") }
        return memberEntity.friends.stream().map { entity -> entity.friendId }
            .collect(Collectors.toList())
    }
}