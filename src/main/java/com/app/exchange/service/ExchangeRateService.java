package com.app.exchange.service;

import com.app.exchange.dto.ExchangeRateResponse;
import com.app.exchange.exception.ExchangeException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.app.exchange.constant.ResponseCode.EXCHANGE_RATE_NOT_FOUND;

@AllArgsConstructor
@Service
@Slf4j
public class ExchangeRateService {

    private final ExchangeClient exchangeClient;

    public ExchangeRateResponse getExchangeRate(String from, String to) throws ExchangeException {
        BigDecimal exchangeRate = exchangeClient.getExchangeRate(from, to);
        if (exchangeRate == null) {
            throw new ExchangeException(EXCHANGE_RATE_NOT_FOUND.name(), EXCHANGE_RATE_NOT_FOUND.getMessage());
        }
        return new ExchangeRateResponse().setRate(exchangeRate);
    }
}
