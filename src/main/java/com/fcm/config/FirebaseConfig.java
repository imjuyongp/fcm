package com.fcm.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Slf4j
@Configuration
public class FirebaseConfig {
    @Value("${firebase.service-account.path}")
    private String SERVICE_ACCOUNT_PATH;

    /**
     * Firebase SDK 초기화 하는 이유
     * 어플리케이션이 Firebase 서비스와 인증된 상태로 통신할 수 있도록 하기 위함
     * "나는 어떤 Firebase 프로젝트를 사용할 것이고, 이 서버가 해당 프로젝트의 권한을 가진 서버임을 증명한다"
     * @return
     */
    @Bean
    public FirebaseApp firebaseApp() {
        try {
            // 옵션 객체 생성 (프로젝트ID, 서비스 계정 정보, 인증정보 등이 내부에 포함)
            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(
                    // 서비스 계정 키 읽기
                    GoogleCredentials.fromStream(
                        new ClassPathResource(SERVICE_ACCOUNT_PATH)
                            .getInputStream())
                )
                .build();

            log.info("Successfully initialized FirebaseApp");
            return FirebaseApp.initializeApp(options); // Firebase SDK 초기화
        } catch (IOException exception) {
            log.error("Error initializing FirebaseApp : {}", exception.getMessage());
            return null;
        }
    }

    /**
     * FirebasMessaging Bean 생성
     * 실제로 FCM 메세지를 보내는 객체
     * Spring이 자동으로 Firebase App 객체를 주입함
     * @param firebaseApp
     * @return
     */
    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }

}
