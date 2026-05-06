package dev.maria.payment.client;

import dev.maria.payment.dto.NotificationRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface NotificationClient {

    @PostExchange("/notifications")
    void send(@RequestBody NotificationRequest request);
}