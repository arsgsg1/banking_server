package com.numble.team2.account

import com.numble.team2.domain.AccountEntity
import com.numble.team2.dto.SendMoneyRequest
import com.numble.team2.repository.AccountRepository
import com.numble.team2.service.AccountService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

@SpringBootTest
class AccountServiceIntegrationTest @Autowired constructor(
	val accountService: AccountService,
	val accountRepository: AccountRepository
) {

	@DisplayName("AccountService는")
	@Nested
	inner class T1 {
		@Test
		fun `잔액이 1000원인 1명에게 100명이 10원씩 동시에 송금하면 잔액이 2000원이 된다`() {
			val money = 10L
			var successCount = AtomicInteger()
			var numberOfExecute = 100
			val executorService = Executors.newFixedThreadPool(10)
			var countDownLatch = CountDownLatch(numberOfExecute)
			for(i: Int in 2..numberOfExecute + 1) {
				executorService.execute {
					try {
						accountService.sendMoney(toAccountId = 1, dto = SendMoneyRequest(fromUserId = 2, fromAccountId = i.toLong(), money = money))
						successCount.getAndIncrement()
					} catch (ex: Exception) {
						println(ex.message)
					}
					countDownLatch.countDown()
				}
			}

			countDownLatch.await()
			val account: AccountEntity? = accountRepository.findById(1).orElse(null)
			assertEquals(account?.balance, 1000 + numberOfExecute * money)
			assertEquals(successCount.get(), numberOfExecute)
		}
	}

}
