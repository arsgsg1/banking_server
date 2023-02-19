package com.numble.team2.domain

import javax.persistence.*

@Entity
@Table(name = "member_friend", indexes = [Index(name = "idx_member_id", columnList = "memberId", unique = false)])
class MemberFriendEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var memberId: Long,
    var friendId: Long
)
