package com.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmService {
    // fcm을 보내는 객체
    private final FirebaseMessaging firebaseMessaging;

    // fcm 토큰을 가진 유저에게 지정된 title과 body를 포함하여 푸시알림 전송
    public void sendNotification(String title, String body, String fcmToken) {
        log.info("Sending notification (title: {}, body: {}, fcmToken: {}", title, body, fcmToken);
        send(createMessage(title, body, fcmToken));

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
