package com.fcm.service;

import com.fcm.domain.User;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 연습용 임시 UserService (DB/JPA 없이 메모리 Map으로 동작)
 * 실제 서비스에서는 UserRepository / FcmTokenRepository(JPA) 로 대체될 자리
 */
@Service
@Slf4j
public class UserService {

    // userId -> User
    private final Map<Long, User> store = new ConcurrentHashMap<>();

    /**
     * 애플리케이션 기동 시 연습용 더미 유저 시딩
     */
    @PostConstruct
    public void seed() {
        User user = new User(1L, "테스트유저");
        // 실제 FCM 토큰으로 바꿔서 테스트하세요. (여러 개 추가 = 멀티 디바이스)
        user.addToken("SAMPLE_FCM_TOKEN_REPLACE_ME");
        store.put(user.getId(), user);
        log.info("[연습용] 더미 유저 {}명 시딩 완료", store.size());
    }

    /**
     * userId로 해당 유저의 FCM 토큰 목록 조회 (발송 플로우의 핵심)
     */
    public List<String> getFcmTokens(Long userId) {
        User user = store.get(userId);
        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다. userId=" + userId);
        }
        return user.getFcmTokens();
    }

    /**
     * 토큰 등록 (클라이언트 → 서버 플로우). 인증된 유저에 토큰을 묶어 저장.
     */
    public void registerToken(Long userId, String token) {
        User user = store.get(userId);
        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다. userId=" + userId);
        }
        user.addToken(token);
    }
}