package com.numble.team2.exception

class AccountNotFoundException(userId: Long) : DomainException("해당 계좌가 존재하지 않습니다. [$userId]")