package com.app.exchange.controller;

import com.app.exchange.dto.*;
import com.app.exchange.exception.ExchangeException;
import com.app.exchange.service.ConversionService;
import com.app.exchange.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping(value = "/v0/currency", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CurrencyController {

    private final ExchangeRateService exchangeRateService;

    private final ConversionService conversionService;

    @GetMapping("/exchange-rate")
    public ResponseEntity<ExchangeRateResponse> exchangeRate(@RequestParam String from,
                                                             @RequestParam String to) throws ExchangeException {
        ExchangeRateResponse exchangeRateResponse = exchangeRateService.getExchangeRate(from, to);
        exchangeRateResponse.setResult(Result.success());
        return ResponseEntity.ok(exchangeRateResponse);
    }

    @PostMapping("/convert")
    public ResponseEntity<CurrencyConversionResponse> convert(@RequestBody CurrencyConversionRequest request) throws ExchangeException {
        CurrencyConversionResponse response = conversionService.convert(request);
        response.setResult(Result.success());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/conversion-history")
    public ResponseEntity<CurrencyConversionHistoryResponse> getConversionHistory(
            @RequestParam(required = false) Long transactionId,
            @RequestParam(required = false) LocalDate transactionDate,
            Pageable pageable
    ) {
        CurrencyConversionHistoryResponse response = conversionService.getConversionHistory(transactionId, transactionDate, pageable);
        response.setResult(Result.success());
        return ResponseEntity.ok(response);
    }
}
