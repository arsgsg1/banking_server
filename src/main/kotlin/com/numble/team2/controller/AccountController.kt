package com.numble.team2.controller

import com.numble.team2.dto.SendMoneyRequest
import com.numble.team2.service.AccountService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class AccountController(
    private val accountService: AccountService
) {
    @PatchMapping("/accounts/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun sendMoney(@PathVariable accountId: Long, @RequestBody dto: SendMoneyRequest): String {
        accountService.sendMoney(accountId, dto)
        return "이체가 완료되었습니다."
    }
}