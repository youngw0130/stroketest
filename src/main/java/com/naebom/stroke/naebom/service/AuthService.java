package com.naebom.stroke.naebom.service;

import com.naebom.stroke.naebom.dto.SignupRequestDto;
import com.naebom.stroke.naebom.dto.LoginRequestDto;
import com.naebom.stroke.naebom.entity.Member;
import com.naebom.stroke.naebom.repository.MemberRepository;
import com.naebom.stroke.naebom.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 회원가입
    public void signup(SignupRequestDto requestDto) {
        // 이메일 있는지 없는지 체크
        if (memberRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 비번 확인
        if (!requestDto.getPassword().equals(requestDto.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        // Member 엔티티 생성 및 필드 매핑
        Member member = new Member();
        member.setName(requestDto.getName());
        member.setEmail(requestDto.getEmail());
        member.setPassword(passwordEncoder.encode(requestDto.getPassword())); // 암호화된 비밀번호 저장
        member.setBirthDate(requestDto.getBirthDate());
        member.setGender(requestDto.getGender());

        memberRepository.save(member);
    }

    // 로그인
    public String login(LoginRequestDto requestDto) {
        // 이메일로 사용자 조회
        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다.");
        }

        // JWT 토큰 생성 후 반환
        return jwtTokenProvider.generateToken(member.getEmail());
    }
}
