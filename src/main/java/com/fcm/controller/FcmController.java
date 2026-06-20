package com.fcm.controller;

import com.fcm.dto.FcmRequestDto;
import com.fcm.service.FcmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/notifications")
@Tag(name = "Notifications", description = "push notification FCM test api")
public class FcmController {

    private final FcmService fcmService;

    @PostMapping("/send")
    @Operation(summary = "특정 사용자에게 푸시 알람 전송", description = "userId로 사용자의 FCM 토큰을 조회하여 푸시알람을 전송합니다.")
    public ResponseEntity<String> sendMessage(@Valid @RequestBody FcmRequestDto request) {
        FcmService.SendResult result = fcmService.sendNotification(request);
        String message = String.format("전송 성공 %d건, 실패 %d건", result.success(), result.fail());

        // 전부 실패하면 실제 실패를 응답에도 반영 (502)
        if (result.success() == 0 && result.fail() > 0) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(message);
        }
        return ResponseEntity.ok(message);
    }
}
