package com.app.exchange.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class CurrencyConversionHistoryResponse  extends BaseResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private List<CurrencyConversionHistoryDto> currencyConversionHistoryDtoList;
}
