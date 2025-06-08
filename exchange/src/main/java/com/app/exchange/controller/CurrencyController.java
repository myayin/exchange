package com.app.exchange.controller;

import com.app.exchange.dto.CurrencyConversionRequest;
import com.app.exchange.dto.CurrencyConversionResponse;
import com.app.exchange.dto.ExchangeRateResponse;
import com.app.exchange.dto.Result;
import com.app.exchange.exception.ExchangeException;
import com.app.exchange.service.ConversionService;
import com.app.exchange.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
}
