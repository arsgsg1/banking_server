package com.numble.team2.dto

// TODO: 알람기능
// TODO: 계좌조회 api
data class NotificationEvent (
    val fromMemberId: Long,
    val toMemberId: Long,
    val money: Long
)