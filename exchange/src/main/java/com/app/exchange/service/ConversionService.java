package com.app.exchange.service;

import com.app.exchange.dto.CurrencyConversionRequest;
import com.app.exchange.dto.CurrencyConversionResponse;
import com.app.exchange.exception.ExchangeException;
import com.app.exchange.model.CurrencyConversionHistory;
import com.app.exchange.repository.CurrencyConversionHistoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.app.exchange.constant.ResponseCode.EXCHANGE_RATE_NOT_FOUND;
import static com.app.exchange.mapper.CurrencyConversionHistoryMapper.getCurrencyConversionHistory;

@AllArgsConstructor
@Service
@Slf4j
public class ConversionService {
    private final ExchangeClient exchangeClient;
    private final CurrencyConversionHistoryRepository currencyConversionHistoryRepository;

    public CurrencyConversionResponse convert(CurrencyConversionRequest request) throws ExchangeException {
        BigDecimal exchangeRate = exchangeClient.getExchangeRate(request.getSourceCurrency(), request.getTargetCurrency());
        if (exchangeRate == null) {
            throw new ExchangeException(EXCHANGE_RATE_NOT_FOUND.name(), EXCHANGE_RATE_NOT_FOUND.getMessage());
        }

        BigDecimal convertedAmount = request.getSourceAmount().multiply(exchangeRate);
        CurrencyConversionHistory history = getCurrencyConversionHistory(request, exchangeRate, convertedAmount);
        history = currencyConversionHistoryRepository.save(history);

        return new CurrencyConversionResponse().setConvertedAmount(convertedAmount)
                .setTransactionId(history.getId());
    }
}
