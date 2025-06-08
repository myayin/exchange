package com.app.exchange.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

import static com.app.exchange.constant.ResponseCode.SUCCESS;
import static com.app.exchange.constant.ResponseCode.SYSTEM_ERROR;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Result implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String code;

    private String message;

    public Result(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Result success() {
        return new Result(SUCCESS.name(), SUCCESS.getMessage());
    }

    public static Result error() {
        return new Result(SYSTEM_ERROR.name(), SYSTEM_ERROR.getMessage());
    }
}
