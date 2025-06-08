package com.app.exchange.service;

import com.app.exchange.dto.CurrencyConversionResponse;
import com.app.exchange.exception.ExchangeException;

public interface ConversionStrategy {
    boolean supports(Object input);
    CurrencyConversionResponse convert(Object input) throws ExchangeException;
}
