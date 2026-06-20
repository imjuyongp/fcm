package com.fcm.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 연습용 임시 User 도메인 (DB 없이 메모리에서만 사용)
 * 한 명의 유저가 여러 디바이스를 가질 수 있으므로 fcmToken은 1:N(List)으로 보관
 */
@Getter
public class User {
    private final Long id;
    private final String name;
    private final List<String> fcmTokens;

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
        this.fcmTokens = new ArrayList<>();
    }

    public void addToken(String token) {
        if (token != null && !token.isBlank() && !fcmTokens.contains(token)) {
            fcmTokens.add(token);
        }
    }

    public void removeToken(String token) {
        fcmTokens.remove(token);
    }
}