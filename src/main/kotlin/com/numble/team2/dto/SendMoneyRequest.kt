package com.numble.team2.dto

data class SendMoneyRequest(
    val fromUserId: Long,
    val fromAccountId: Long,
    val money: Long
)