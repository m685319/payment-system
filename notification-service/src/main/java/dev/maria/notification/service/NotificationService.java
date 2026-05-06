package dev.maria.notification.service;

import dev.maria.notification.dto.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    public void send(NotificationRequest request) {
        log.info("Sending notification to {}: {}", request.email(), request.message());
    }
}