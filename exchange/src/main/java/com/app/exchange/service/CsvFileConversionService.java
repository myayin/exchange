package com.app.exchange.service;

import com.app.exchange.dto.CurrencyConversionDto;
import com.app.exchange.dto.CurrencyConversionRequest;
import com.app.exchange.dto.CurrencyConversionResponse;
import com.app.exchange.exception.ExchangeException;
import com.app.exchange.repository.CurrencyConversionHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvFileConversionService extends BaseConversionService implements ConversionStrategy {

    public CsvFileConversionService(ExchangeClient exchangeClient,
                                    CurrencyConversionHistoryRepository repository) {
        super(exchangeClient, repository);
    }

    @Override
    public boolean supports(Object input) {
        return input instanceof MultipartFile;
    }

    @Override
    public CurrencyConversionResponse convert(Object input) {
        MultipartFile file = (MultipartFile) input;
        List<CurrencyConversionDto> dtos = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                // header
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                CurrencyConversionRequest request = getCurrencyConversionRequest(line);
                if (request == null) continue;

                CurrencyConversionDto dto = getCurrencyConversionDto(request);
                dtos.add(dto);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV file", e);
        } catch (ExchangeException e) {
            throw new RuntimeException(e);
        }
        return new CurrencyConversionResponse()
                .setCurrencyConversionDtoList(dtos);
    }

    private static CurrencyConversionRequest getCurrencyConversionRequest(String line) {
        String[] tokens = line.split(";");
        if (tokens.length != 3) return null;

        String sourceCurrency = tokens[0].trim();
        String targetCurrency = tokens[1].trim();
        BigDecimal sourceAmount = new BigDecimal(tokens[2].trim());

        return new CurrencyConversionRequest()
                .setSourceAmount(sourceAmount)
                .setSourceCurrency(sourceCurrency)
                .setTargetCurrency(targetCurrency);
    }
}
