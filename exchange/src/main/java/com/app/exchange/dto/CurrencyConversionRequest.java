package com.app.exchange.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CurrencyConversionRequest {
    private BigDecimal sourceAmount;
    private String sourceCurrency;
    private String targetCurrency;
}
