package com.app.exchange.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Accessors(chain = true)
public class ExchangeRateResponse extends BaseResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private BigDecimal rate;
}
