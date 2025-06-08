package com.app.exchange.constant;

import lombok.Getter;

@Getter
public enum ResponseCode {
    SUCCESS("Success"), SYSTEM_ERROR(
            "We are unable to process your request at the moment. Please try again later."),
    EXCHANGE_RATE_NOT_FOUND("Requested exchange rate data is unavailable or does not exist.");

    private final String message;

    ResponseCode(String message) {
        this.message = message;
    }
}
