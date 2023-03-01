package com.numble.team2.exception

class MemberNotFoundException(memberId: Long) : DomainException("해당 ID의 유저를 찾을 수 없습니다. $memberId")