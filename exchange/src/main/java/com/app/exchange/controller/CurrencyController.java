package com.app.exchange.controller;

import com.app.exchange.dto.*;
import com.app.exchange.exception.ExchangeException;
import com.app.exchange.service.ConversionHistoryService;
import com.app.exchange.service.ConversionStrategyContext;
import com.app.exchange.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/v0/currency", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CurrencyController {

    private final ExchangeRateService exchangeRateService;

    private final ConversionStrategyContext conversionStrategyContext;

    private final ConversionHistoryService conversionHistoryService;

    @GetMapping("/exchange-rate")
    public ResponseEntity<ExchangeRateResponse> exchangeRate(@RequestParam
                                                             @Schema(description = "Currency code to convert from", example = "USD")
                                                             String from,

                                                             @RequestParam
                                                             @Schema(description = "Currency code to convert to", example = "EUR")
                                                             String to) throws ExchangeException {
        ExchangeRateResponse exchangeRateResponse = exchangeRateService.getExchangeRate(from, to);
        exchangeRateResponse.setResult(Result.success());
        return ResponseEntity.ok(exchangeRateResponse);
    }

    @PostMapping("/convert")
    public ResponseEntity<CurrencyConversionResponse> convert(@RequestBody CurrencyConversionRequest request) throws ExchangeException {
        CurrencyConversionResponse response = conversionStrategyContext.convert(request);
        response.setResult(Result.success());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/bulk-convert")
    public ResponseEntity<List<CurrencyConversionResponse>> convertBulk(
            @RequestParam("file") MultipartFile file) throws ExchangeException {
        CurrencyConversionResponse response = conversionStrategyContext.convert(file);
        response.setResult(Result.success());
        return ResponseEntity.ok(List.of(response));
    }

    @GetMapping("/conversion-history")
    public ResponseEntity<CurrencyConversionHistoryResponse> getConversionHistory(
            @Schema(description = "Transaction ID", example = "1")
            @RequestParam(required = false) Long transactionId,

            @Schema(description = "Transaction date in ISO format (yyyy-MM-dd)", example = "2025-06-09")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate transactionDate,
            @ParameterObject
            @PageableDefault Pageable pageable
    ) {
        CurrencyConversionHistoryResponse response = conversionHistoryService.getConversionHistory(transactionId, transactionDate, pageable);
        response.setResult(Result.success());
        return ResponseEntity.ok(response);
    }
}
