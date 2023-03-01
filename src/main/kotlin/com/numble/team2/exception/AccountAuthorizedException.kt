package com.numble.team2.exception

class AccountAuthorizedException(accountId: Long) : DomainException("해당 계좌의 소유자가 아닙니다. $accountId")