package com.app.exchange.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
public class ExchangeClientResponse {
    private boolean success;
    private long timestamp;
    private String source;
    private Map<String, BigDecimal> quotes;
}
