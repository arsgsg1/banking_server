package com.numble.team2.account

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.numble.team2.domain.AccountEntity
import com.numble.team2.dto.SendMoneyRequest
import com.numble.team2.exception.AccountAuthorizedException
import com.numble.team2.repository.AccountRepository
import com.numble.team2.service.AccountService
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.context.ApplicationEventPublisher

class AccountServiceTest {
    val accountRepository: AccountRepository = mock()
    val eventPublisher: ApplicationEventPublisher = mock()
    val accountService = AccountService(accountRepository, eventPublisher)
    @Nested
    @DisplayName("AccountService는")
    inner class T1 {
        @Test
        fun `송금 계좌의 owner_id와 입력받은 member_id가 다르면 AccountAuthorizedException을 던진다`() {
            // given
            val accountEntity = AccountEntity(id = 1, ownerId = 1)
            val dto = SendMoneyRequest(fromUserId = 2, fromAccountId = 1, money = 10)
            whenever(accountRepository.findByIdForUpdate(any())).thenReturn(accountEntity)
            // when, then
            assertThrows(AccountAuthorizedException::class.java) {
                accountService.sendMoney(any(), dto)
            }
        }

        @Test
        fun `송금이 성공하면 보낸 돈만큼 송금 계좌의 잔액이 감소하고 수금 계좌의 잔액은 늘어난다`() {

        }
    }
}