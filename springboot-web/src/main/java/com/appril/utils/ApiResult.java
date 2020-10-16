package com.appril.utils;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@AllArgsConstructor
@Setter
@Getter
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int CODE_OK = 200;
    public static final int CODE_ERR = 500;

    private int status = ApiResult.CODE_OK; //ok
    private String token;
    private String message;
    private T data;


    public static <T> ApiResult<T> isOk(String token, String message, T data) {
        return new ApiResult<>(CODE_OK, token, message, data);
    }

    public static <T> ApiResult<T> isOkNoToken(String message, T data) {
        return new ApiResult<T>(CODE_OK, "", message, data);
    }

    public static <T> ApiResult<T> isOkMessage(String message) {
        return new ApiResult<>(CODE_OK, "", message, null);
    }

    public static <T> ApiResult<T> isErr(String token, String message, T data) {
        return new ApiResult<>(CODE_ERR, token, message, data);
    }

    public static <T> ApiResult<T> isErrNoToken(String message, T data) {
        return new ApiResult<>(CODE_ERR, "", message, data);
    }

    public static <T> ApiResult<T> isErrTokenAndMsg(String token, String message) {
        return new ApiResult<>(CODE_ERR, token, message, null);
    }

    public static <T> ApiResult<T> isErrMessage(String message) {
        return new ApiResult<>(CODE_ERR, "", message, null);
    }



}
