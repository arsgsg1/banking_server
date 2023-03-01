package com.numble.team2.repository

import com.numble.team2.domain.AccountEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.LockModeType

@Repository
interface AccountRepository : JpaRepository<AccountEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM AccountEntity a WHERE a.id = :accountId")
    fun findByIdForUpdate(accountId: Long): AccountEntity?
}