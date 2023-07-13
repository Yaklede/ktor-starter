package com.example.member.service

import com.example.member.repository.MemberCondition
import com.example.member.repository.MemberDto
import com.example.member.repository.MemberRepository

class MemberService(
    private val memberRepository: MemberRepository
) {
    suspend fun getMemberById(id: Long): MemberDto {
        return memberRepository.findById(id) ?:  throw IllegalArgumentException("Member not found")
    }

    suspend fun save(condition: MemberCondition): Long {
        return memberRepository.save(condition)
    }

    suspend fun delete(id: Long) {
        memberRepository.delete(id)
    }
}