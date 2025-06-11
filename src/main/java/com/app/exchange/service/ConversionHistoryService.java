package com.app.exchange.service;

import com.app.exchange.dto.CurrencyConversionHistoryDto;
import com.app.exchange.dto.CurrencyConversionHistoryResponse;
import com.app.exchange.mapper.CurrencyConversionHistoryResponseMapper;
import com.app.exchange.model.CurrencyConversionHistory;
import com.app.exchange.repository.CurrencyConversionHistoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class ConversionHistoryService {
    private final CurrencyConversionHistoryRepository repository;


    public CurrencyConversionHistoryResponse getConversionHistory(Long transactionId, LocalDate date,
                                                                  Pageable pageable) {
        List<CurrencyConversionHistory> histories;

        if (transactionId != null && date != null) {
            Instant start = date.atStartOfDay(ZoneOffset.UTC).toInstant();
            Instant end = date.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();
            histories = repository.findByIdAndTransactionDateBetween(transactionId, start, end, pageable);
        } else if (transactionId != null) {
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
