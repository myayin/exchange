package com.app.exchange.mapper;

import com.app.exchange.dto.CurrencyConversionHistoryDto;
import com.app.exchange.model.CurrencyConversionHistory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CurrencyConversionHistoryResponseMapper {
    CurrencyConversionHistoryResponseMapper INSTANCE = Mappers.getMapper(CurrencyConversionHistoryResponseMapper.class);

    CurrencyConversionHistoryDto toResponse(CurrencyConversionHistory history);

}
