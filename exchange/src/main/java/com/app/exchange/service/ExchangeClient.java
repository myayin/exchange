package com.app.exchange.service;

import com.app.exchange.dto.ExchangeClientResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static com.app.exchange.constant.Constant.BASE_URL;

@Service
@Slf4j
public class ExchangeClient {

    private final RestTemplate restTemplate;

    public ExchangeClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ExchangeClientResponse fetchRates(String source, String to) {
        String url = String.format(
                BASE_URL + "&currencies=%s&source=%s&format=1",
                to.toUpperCase(), source.toUpperCase()
        );
        return restTemplate.getForObject(url, ExchangeClientResponse.class);
    }

    public BigDecimal getExchangeRate(String source, String to) {
        ExchangeClientResponse response = fetchRates(source, to);
        if (response != null && response.isSuccess()) {
            return response.getQuotes().get(source.toUpperCase() + to.toUpperCase());
        }
        return null;
    }
}

