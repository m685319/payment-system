package dev.maria.exception;

import java.util.UUID;

public class OrderNotFoundException extends Exception {
    public OrderNotFoundException(UUID id) {
        super("Order not found: " + id);
    }
}