package com.numble.team2.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class MemberEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    email: String,
    password: String
) {
    @Column(unique = true)
    var email: String = email
        protected set
    var password: String = password
        protected set
}