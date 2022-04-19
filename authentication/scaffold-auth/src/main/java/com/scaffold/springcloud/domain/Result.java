package com.scaffold.springcloud.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hui.zhang
 * @date 2022年04月17日 08:38
 */
@Data
public class Result<T> implements Serializable {

    private int code;
    private String msg;
    private T data;

    public static <T> Result<T> success(T data) {
        Result result = new Result();
        result.code = 0;
        result.msg = "SUCCESS";
        result.data = data;
        return result;
    }
}
