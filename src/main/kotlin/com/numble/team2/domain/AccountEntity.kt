package com.numble.team2.domain

import com.numble.team2.exception.AccountAuthorizedException
import com.numble.team2.exception.NotEnoughBalanceException
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*

@Entity
@DynamicUpdate
@Table(name = "account", indexes = [Index(name = "idx_account_member_id", columnList = "ownerId")])
class AccountEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val ownerId: Long,
) {
    var balance: Long = 0
        protected set

    fun income(money: Long) {
        balance += money
    }
    fun outcome(money: Long) {
        if(balance - money < 0) {
            throw NotEnoughBalanceException()
        }
        balance -= money
    }

    fun checkOwnerBy(userId: Long) {
        if(userId != ownerId) {
            throw AccountAuthorizedException(userId)
        }
    }
}