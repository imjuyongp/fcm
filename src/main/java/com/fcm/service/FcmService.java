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
    // 성공/실패 개수를 반환해 호출자가 실제 결과를 알 수 있게 한다.
    public SendResult sendNotification(FcmRequestDto request) {
        List<String> fcmTokens = userService.getFcmTokens(request.getUserId());
        log.info("Sending notification (title: {}, body: {}, userId: {}, tokenCount: {})",
            request.getTitle(), request.getBody(), request.getUserId(), fcmTokens.size());

        int success = 0;
        int fail = 0;
        for (String token : fcmTokens) {
            if (send(createMessage(request.getTitle(), request.getBody(), token))) {
                success++;
            } else {
                fail++;
            }
        }
        return new SendResult(success, fail);
    }

    // 메세지 전송. 성공 true / 실패 false
    private boolean send(Message message) {
        try {
            String response = firebaseMessaging.send(message);
            log.info("Successfully send Notification: {}", response);
            return true;
        } catch (FirebaseMessagingException e) {
            log.error("Failed to send Notification: {}", e.getMessage());
            return false;
        }
    }

    // 전송 결과 요약
    public record SendResult(int success, int fail) {
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