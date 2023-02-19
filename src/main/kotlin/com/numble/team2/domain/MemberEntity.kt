package com.numble.team2.domain

import javax.persistence.*

@Entity
@Table(name = "member")
class MemberEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    email: String,
    password: String,
    @OneToMany(mappedBy = "memberId", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var friends: MutableList<MemberFriendEntity> = mutableListOf()
) {
    @Column(unique = true)
    var email: String = email
        protected set
    var password: String = password
        protected set
}
