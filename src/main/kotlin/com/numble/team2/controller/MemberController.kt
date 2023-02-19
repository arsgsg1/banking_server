package com.numble.team2.controller

import com.numble.team2.dto.SignUpRequest
import com.numble.team2.service.MemberService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
    private val memberService: MemberService
) {
    @PostMapping("/members")
    @ResponseStatus(code = HttpStatus.CREATED)
    fun SignUp(@RequestBody dto: SignUpRequest) {
        memberService.createMember(dto)
    }
}