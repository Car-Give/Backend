package com.example.cargive.domain.member.repository;

import com.example.cargive.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsMemberByLoginId(String loginId);
    Optional<Member> findMemberByLoginId(String loginId);
}
