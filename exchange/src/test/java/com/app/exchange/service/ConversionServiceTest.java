package com.app.exchange.service;

import com.app.exchange.dto.CurrencyConversionRequest;
import com.app.exchange.dto.CurrencyConversionResponse;
import com.app.exchange.exception.ExchangeException;
import com.app.exchange.model.CurrencyConversionHistory;
import com.app.exchange.repository.CurrencyConversionHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class ConversionServiceTest {

    @Mock
    private ExchangeClient exchangeClient;

    @Mock
    private CurrencyConversionHistoryRepository repository;

    @InjectMocks
    private ConversionService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void convert_shouldReturnResponse_whenExchangeRateIsAvailable() throws ExchangeException {
        // Arrange
        CurrencyConversionRequest request = new CurrencyConversionRequest();
        request.setSourceAmount(new BigDecimal("100"));
        request.setSourceCurrency("USD");
        request.setTargetCurrency("EUR");

        BigDecimal mockRate = new BigDecimal("0.85");
        BigDecimal expectedConverted = new BigDecimal("85.00");

        CurrencyConversionHistory savedHistory = new CurrencyConversionHistory();
        savedHistory.setId(1L);
        savedHistory.setAmount(request.getSourceAmount());
        savedHistory.setConvertedAmount(expectedConverted);
        savedHistory.setSourceCurrency("USD");
        savedHistory.setTargetCurrency("EUR");

        when(exchangeClient.getExchangeRate("USD", "EUR")).thenReturn(mockRate);
        when(repository.save(Mockito.any())).thenReturn(savedHistory);

        // Act
        CurrencyConversionResponse response = service.convert(request);

        // Assert
        assertNotNull(response);
        assertEquals(expectedConverted, response.getConvertedAmount());
        assertEquals(1L, response.getTransactionId());
    }

    @Test
    void convert_shouldThrowException_whenExchangeRateIsNull() {
        // Arrange
        CurrencyConversionRequest request = new CurrencyConversionRequest();
        request.setSourceAmount(new BigDecimal("100"));
        request.setSourceCurrency("USD");
        request.setTargetCurrency("EUR");

        when(exchangeClient.getExchangeRate("USD", "EUR")).thenReturn(null);

        // Act & Assert
        ExchangeException ex = assertThrows(ExchangeException.class, () -> service.convert(request));

        assertEquals("EXCHANGE_RATE_NOT_FOUND", ex.getErrorCode());
    }
}

