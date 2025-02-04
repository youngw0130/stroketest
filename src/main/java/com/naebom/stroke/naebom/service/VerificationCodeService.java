package com.naebom.stroke.naebom.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class VerificationCodeService {

    private final StringRedisTemplate redisTemplate;
    private final JavaMailSender mailSender;

    public VerificationCodeService(StringRedisTemplate redisTemplate, JavaMailSender mailSender) {
        this.redisTemplate = redisTemplate;
        this.mailSender = mailSender;
    }

    // 인증 코드 생성 & 이메일 발송
    public String generateAndSendCode(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("이메일이 유효하지 않습니다.");
        }

        String code = generateCode(); // 인증 코드 생성
        saveCodeToRedis(email, code); // Redis에 인증 코드 저장
        sendEmail(email, code); // 이메일 발송

        return code; // 생성된 인증 코드 반환 (디버깅용)
    }

    // 인증 코드 검증
    public void verifyCode(String email, String verificationCode) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("이메일이 유효하지 않습니다.");
        }
        if (verificationCode == null || verificationCode.isEmpty()) {
            throw new IllegalArgumentException("인증 코드가 유효하지 않습니다.");
        }

        String storedCode = redisTemplate.opsForValue().get(email); // Redis에서 인증 코드 가져오기
        if (storedCode == null) {
            throw new IllegalArgumentException("인증번호가 만료되었거나 존재하지 않습니다.");
        }
        if (!storedCode.equals(verificationCode)) {
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
        }

        // 인증 성공 후 Redis에서 삭제
        redisTemplate.delete(email);
    }

    // 이메일 발송
    private void sendEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Naebom 인증 코드");
        message.setText("인증 코드: " + code + "\n인증 코드는 5분간 유효합니다.");

        try {
            mailSender.send(message); // 이메일 발송
        } catch (Exception e) {
            throw new RuntimeException("이메일 전송 중 오류가 발생했습니다.", e);
        }
    }

    // 인증 코드 생성
    private String generateCode() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }

    // Redis에 인증 코드 저장
    private void saveCodeToRedis(String email, String code) {
        redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES); // 인증 코드 5분 유효
    }
}
