package com.app.exchange.service;

import com.app.exchange.dto.CurrencyConversionResponse;
import com.app.exchange.exception.ExchangeException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversionStrategyContext {

    private final List<ConversionStrategy> strategies;

    public ConversionStrategyContext(List<ConversionStrategy> strategies) {
        this.strategies = strategies;
    }

    public CurrencyConversionResponse convert(Object input) throws ExchangeException {
        return strategies.stream()
                .filter(s -> s.supports(input))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No strategy found for input"))
                .convert(input);
    }
}
