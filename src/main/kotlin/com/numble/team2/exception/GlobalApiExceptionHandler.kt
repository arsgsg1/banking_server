package com.numble.team2.exception

import com.numble.team2.dto.CommonResponse
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalApiExceptionHandler {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @ExceptionHandler(DomainException::class)
    fun handlingDomainException(ex: DomainException): CommonResponse<Unit> {
        return CommonResponse.of(ex.message)
    }
}