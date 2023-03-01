package com.numble.team2.service

import com.numble.team2.dto.SendMoneyRequest
import com.numble.team2.exception.AccountNotFoundException
import com.numble.team2.repository.AccountRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService(
    private val accountRepository: AccountRepository
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Transactional
    fun sendMoney(toAccountId: Long, dto: SendMoneyRequest) {
        val fromAccount = accountRepository.findByIdForUpdate(dto.fromAccountId) ?: throw AccountNotFoundException(dto.fromAccountId)
        fromAccount.checkOwnerBy(dto.fromUserId)
        val toAccount = accountRepository.findByIdForUpdate(toAccountId) ?: throw AccountNotFoundException(toAccountId)
        fromAccount.outcome(dto.money)
        toAccount.income(dto.money)
        logger.info("${dto.fromUserId} 번 유저가 $toAccountId 번 계좌에 ${dto.money} 원을 이체함")
    }
}
