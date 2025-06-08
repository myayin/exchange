package com.app.exchange.mapper;

import com.app.exchange.dto.CurrencyConversionRequest;
import com.app.exchange.model.CurrencyConversionHistory;

import java.math.BigDecimal;


public class CurrencyConversionHistoryMapper {
    public static CurrencyConversionHistory getCurrencyConversionHistory(CurrencyConversionRequest request, BigDecimal exchangeRate, BigDecimal convertedAmount) {
        return new CurrencyConversionHistory()
                .setExchangeRate(exchangeRate)
                .setSourceCurrency(request.getSourceCurrency())
                .setTargetCurrency(request.getTargetCurrency())
                .setConvertedAmount(convertedAmount)
                .setAmount(request.getSourceAmount());
    }
}
