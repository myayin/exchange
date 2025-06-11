package com.app.exchange.exception;

import lombok.Getter;

@Getter
public class ExchangeException extends Exception {

    private final String errorCode;

    public ExchangeException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
