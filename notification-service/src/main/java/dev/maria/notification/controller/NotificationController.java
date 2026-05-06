package dev.maria.notification.controller;

import dev.maria.notification.dto.NotificationRequest;
import dev.maria.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @PostMapping
    public ResponseEntity<Void> send(@RequestBody NotificationRequest request) {
        service.send(request);
        return ResponseEntity.ok().build();
    }
}
