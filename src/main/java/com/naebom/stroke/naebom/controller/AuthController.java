package com.naebom.stroke.naebom.controller;

import com.naebom.stroke.naebom.dto.SignupRequestDto;
import com.naebom.stroke.naebom.dto.LoginRequestDto;
import com.naebom.stroke.naebom.service.AuthService;
import com.naebom.stroke.naebom.service.VerificationCodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final VerificationCodeService verificationCodeService;

    public AuthController(AuthService authService, VerificationCodeService verificationCodeService) {
        this.authService = authService;
        this.verificationCodeService = verificationCodeService;
    }

    // 이메일로 인증 코드 전송
    @PostMapping("/send-verification")
    public ResponseEntity<Map<String, String>> sendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = verificationCodeService.generateAndSendCode(email); // 인증 코드 생성
        Map<String, String> response = new HashMap<>();
        response.put("message", "인증번호가 이메일로 전송되었습니다.");
        return ResponseEntity.ok(response);
    }

    // 회원가입 (인증 코드 검증 포함)
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestBody SignupRequestDto requestDto) {
        try {
            // 인증 코드 검증
            verificationCodeService.verifyCode(requestDto.getEmail(), requestDto.getVerificationCode());
            // 회원가입
            authService.signup(requestDto);

            Map<String, String> response = new HashMap<>();
            response.put("message", "회원가입 성공!");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDto requestDto) {
        try {
            String token = authService.login(requestDto);
            Map<String, String> response = new HashMap<>();
            response.put("message", "로그인 성공!");
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
