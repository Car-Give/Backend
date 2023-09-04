package com.example.cargive.global.result;

import lombok.Getter;

@Getter
public class ResultResponse {

    private final String code;
    private final String message;
    private final Object data;

    public static ResultResponse of(ResultCode resultCode){
        return new ResultResponse(resultCode, "");
    }
    public static ResultResponse of(ResultCode resultCode, Object data) {
        return new ResultResponse(resultCode, data);
    }

    public ResultResponse(ResultCode resultCode, Object data){
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }
}
