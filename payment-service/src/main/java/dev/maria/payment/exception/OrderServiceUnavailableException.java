package dev.maria.payment.exception;

public class OrderServiceUnavailableException extends RuntimeException {

    public OrderServiceUnavailableException() {
        super("Order service unavailable");
    }
}