package com.fcm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

@Getter
@NoArgsConstructor
public class FcmRequestDto {
    @Schema(description = "사용자ID", example = "1")
    private long userId;

    @Schema(description = "알람 제목", example = "알람 테스트")
    private String title;

    @Schema(description = "알람 본문", example = "알람 내용 테스트")
    private String body;

    /**
     * 서비스 내에서 개발 시 토큰을 DTO로 전달?
     * FCM 토큰을 별도 테이블에서 관리 -> users테이블과 1:N으로 매핑되어 있음
     * user의 인증정보를 받아 토큰 별도 조회 -> 따라서 dto에서는 사용자의 정보를 받아서 전달 후 FCM 토큰 추출
     */
    // @Schema(description = "FCM 토큰", example = "aaa")
    // private String fcmToken;

}
