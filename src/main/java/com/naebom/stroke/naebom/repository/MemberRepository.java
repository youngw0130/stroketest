package com.naebom.stroke.naebom.repository;

import com.naebom.stroke.naebom.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
//Long은 기본키(id) 타입
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}
