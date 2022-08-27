package com.scaffold.webflux.exception;

import lombok.Getter;
import lombok.Setter;


public class BusinessException extends RuntimeException {

    @Getter
    @Setter
    private int code;
    @Getter
    @Setter
    private String msg;

    public BusinessException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}
