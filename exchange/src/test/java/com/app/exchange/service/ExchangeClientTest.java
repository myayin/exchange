package com.app.exchange.service;

import com.app.exchange.dto.ExchangeClientResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExchangeClientTest {
    private RestTemplate restTemplate;
    private ExchangeClient exchangeClient;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        exchangeClient = new ExchangeClient(restTemplate);
    }
    @Test
    void getExchangeRate_shouldReturnExchangeRate_whenSuccessfulResponse() {
        // Arrange
        ExchangeClientResponse mockResponse = new ExchangeClientResponse();
        mockResponse.setSuccess(true);
        mockResponse.setQuotes(Map.of("USDEUR", BigDecimal.valueOf(0.94)));

        when(restTemplate.getForObject(
                ArgumentMatchers.contains("currencies=EUR"),
                eq(ExchangeClientResponse.class)
        )).thenReturn(mockResponse);

        // Act
        BigDecimal rate = exchangeClient.getExchangeRate("USD", "EUR");

        // Assert
        assertNotNull(rate);
        assertEquals(BigDecimal.valueOf(0.94), rate);
    }

    @Test
    void getExchangeRate_shouldReturnNull_whenResponseIsNull() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(ExchangeClientResponse.class)))
                .thenReturn(null);

        // Act
        BigDecimal rate = exchangeClient.getExchangeRate("USD", "EUR");

        // Assert
        assertNull(rate);
    }

    @Test
    void getExchangeRate_shouldReturnNull_whenUnsuccessfulResponse() {
        // Arrange
        ExchangeClientResponse mockResponse = new ExchangeClientResponse();
        mockResponse.setSuccess(false); // success = false

        when(restTemplate.getForObject(anyString(), eq(ExchangeClientResponse.class)))
                .thenReturn(mockResponse);

        // Act
        BigDecimal rate = exchangeClient.getExchangeRate("USD", "EUR");

        // Assert
        assertNull(rate);
    }
}

