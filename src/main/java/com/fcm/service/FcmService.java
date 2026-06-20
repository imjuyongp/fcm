package com.fcm.service;

import com.fcm.dto.FcmRequestDto;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmService {
    // fcm을 보내는 객체
    private final FirebaseMessaging firebaseMessaging;
    // 임의의(연습용) userService - userId로 토큰 조회
    private final UserService userService;

    // userId로 토큰을 조회하여, 해당 유저의 모든 디바이스에 푸시알림 전송 (1:N)
    public void sendNotification(FcmRequestDto request) {
        List<String> fcmTokens = userService.getFcmTokens(request.getUserId());
        log.info("Sending notification (title: {}, body: {}, userId: {}, tokenCount: {})",
            request.getTitle(), request.getBody(), request.getUserId(), fcmTokens.size());

        fcmTokens.forEach(token ->
            send(createMessage(request.getTitle(), request.getBody(), token)));
    }

    // 메세지 전송
    private void send(Message message) {
        try {
            String response = firebaseMessaging.send(message);
            log.info("Successfully send Notification: {}", response);
        } catch (FirebaseMessagingException e) {
            log.error("Failed to send Notification: {}", e.getMessage());
        }
    }

    // Message 객체 생성
    private Message createMessage(String title, String body, String fcmToken) {
        return Message.builder()
            .putData("title", title)
            .putData("body", body)
            .setToken(fcmToken)
            .build();
    }

}