package com.numble.team2.dto

import org.springframework.http.HttpStatus

data class CommonResponse<T>(
    val status: String,
    val code: Int,
    val data: T? = null,
    val debugMessage: String?
) {
    companion object {
        fun <T> of(message: String?): CommonResponse<T> {
            return CommonResponse(
                status = "failed",
                code = HttpStatus.BAD_REQUEST.value(),
                debugMessage = message
            )
        }
    }
}
