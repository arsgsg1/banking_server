package com.numble.team2.dto

import com.numble.team2.domain.MemberFriendEntity

data class FriendsResponse(
    val id: Long,
    val email: String
) {
    companion object {
        fun fromEntity(entity: MemberFriendEntity): FriendsResponse {
            return FriendsResponse(id = entity.friendId, email = entity.friendEmail)
        }
    }
}
