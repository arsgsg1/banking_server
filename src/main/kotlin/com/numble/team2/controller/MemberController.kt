package com.numble.team2.controller

import com.numble.team2.dto.FriendsResponse
import com.numble.team2.dto.MemberFriendRequest
import com.numble.team2.dto.SignUpRequest
import com.numble.team2.service.MemberService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/members")
class MemberController(
    private val memberService: MemberService
) {
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    fun signUp(@RequestBody dto: SignUpRequest) {
        memberService.createMember(dto)
    }

    @PostMapping("/{memberId}/friends")
    @ResponseStatus(code = HttpStatus.CREATED)
    fun createMemberFriend(@PathVariable memberId: Long, @RequestBody dto: MemberFriendRequest) {
        memberService.createFriend(memberId, dto)
    }

    @GetMapping("/{memberId}/friends")
    @ResponseStatus(code = HttpStatus.OK)
    fun getAllMemberFriends(@PathVariable memberId: Long): List<FriendsResponse> {
        return memberService.getAllFriends(memberId)
    }
}
