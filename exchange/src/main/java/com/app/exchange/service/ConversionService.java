package com.app.exchange.service;

import com.app.exchange.dto.CurrencyConversionHistoryDto;
import com.app.exchange.dto.CurrencyConversionHistoryResponse;
import com.app.exchange.dto.CurrencyConversionRequest;
import com.app.exchange.dto.CurrencyConversionResponse;
import com.app.exchange.exception.ExchangeException;
import com.app.exchange.mapper.CurrencyConversionHistoryResponseMapper;
import com.app.exchange.model.CurrencyConversionHistory;
import com.app.exchange.repository.CurrencyConversionHistoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;

import static com.app.exchange.constant.ResponseCode.EXCHANGE_RATE_NOT_FOUND;
import static com.app.exchange.mapper.CurrencyConversionHistoryMapper.getCurrencyConversionHistory;

@AllArgsConstructor
@Service
@Slf4j
public class ConversionService {
    private final ExchangeClient exchangeClient;
    private final CurrencyConversionHistoryRepository repository;

    public CurrencyConversionResponse convert(CurrencyConversionRequest request) throws ExchangeException {
        BigDecimal exchangeRate = exchangeClient.getExchangeRate(request.getSourceCurrency(), request.getTargetCurrency());
        if (exchangeRate == null) {
            throw new ExchangeException(EXCHANGE_RATE_NOT_FOUND.name(), EXCHANGE_RATE_NOT_FOUND.getMessage());
        }

        BigDecimal convertedAmount = request.getSourceAmount().multiply(exchangeRate);
        CurrencyConversionHistory history = getCurrencyConversionHistory(request, exchangeRate, convertedAmount);
        history = repository.save(history);

        return new CurrencyConversionResponse().setConvertedAmount(convertedAmount)
                .setTransactionId(history.getId());
    }

    public CurrencyConversionHistoryResponse getConversionHistory(Long transactionId, LocalDate date,
                                                                  Pageable pageable) {
        List<CurrencyConversionHistory> histories;

        if (transactionId != null) {
            histories = repository.findById(transactionId)
                    .map(List::of)
                    .orElse(Collections.emptyList());
        } else if (date != null) {
            Instant start = date.atStartOfDay(ZoneOffset.UTC).toInstant();
            Instant end = date.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();
            histories = repository.findByTransactionDateBetween(start, end, pageable);
        } else {
            throw new IllegalArgumentException("At least one filter (transactionId or date) must be provided.");
        }

        List<CurrencyConversionHistoryDto> dtoList = histories.stream()
                .map(CurrencyConversionHistoryResponseMapper.INSTANCE::toResponse)
                .toList();

        return new CurrencyConversionHistoryResponse().setCurrencyConversionHistoryDtoList(dtoList);
    }
}
