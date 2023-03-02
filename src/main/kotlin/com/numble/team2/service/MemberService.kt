package com.numble.team2.service

import com.numble.team2.domain.MemberEntity
import com.numble.team2.domain.MemberFriendEntity
import com.numble.team2.dto.FriendsResponse
import com.numble.team2.dto.MemberFriendRequest
import com.numble.team2.dto.SignUpRequest
import com.numble.team2.exception.DomainException
import com.numble.team2.exception.MemberNotFoundException
import com.numble.team2.repository.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalArgumentException
import java.util.stream.Collectors

@Service
class MemberService(
    private val memberRepository: MemberRepository,
) {
    @Transactional(readOnly = true)
    fun isNotDuplicateEmail(email: String): Boolean = memberRepository.findByEmail(email) == null

    @Transactional
    fun createMember(dto: SignUpRequest): Long {
        require(isNotDuplicateEmail(dto.email)) { throw DomainException("이미 존재하는 이메일입니다.") }
        val memberEntity = MemberEntity(email = dto.email, password = dto.password)
        return memberRepository.save(memberEntity).id
    }

    @Transactional
    fun createFriend(memberId: Long, dto: MemberFriendRequest) {
        checkSelfCreateFriend(memberId, dto.friendId)
        val memberEntity = memberRepository.findMemberWithAllFriends(memberId)
            ?: throw IllegalArgumentException("해당 ID의 유저가 없습니다. [${memberId}]")

        val friendEntity = memberRepository.findByIdOrNull(dto.friendId)
            ?: throw MemberNotFoundException(dto.friendId)

        memberEntity.friends.firstOrNull { entity -> entity.friendId == dto.friendId } ?: run {
            memberEntity.friends.add(
                MemberFriendEntity(
                    memberId = memberId,
                    friendId = friendEntity.id,
                    friendEmail = friendEntity.email
                )
            )
        }
    }

    @Transactional(readOnly = true)
    fun getAllFriends(memberId: Long): List<FriendsResponse> {
        val memberEntity = memberRepository.findMemberWithAllFriends(memberId)
            ?: throw MemberNotFoundException(memberId)
        return memberEntity.friends.map { entity -> FriendsResponse.fromEntity(entity) }
    }

    private fun checkSelfCreateFriend(memberId: Long, friendId: Long) =
        require(memberId == friendId) { throw DomainException("자기 자신은 친구 추가를 할 수 없습니다.") }
}