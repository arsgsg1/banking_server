package com.numble.team2.domain

import javax.persistence.*

@Entity
@Table(name = "notification_history")
class NotificationHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_member_id")
    var from: MemberEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_member_id")
    var to: MemberEntity,
    var money: Long
)