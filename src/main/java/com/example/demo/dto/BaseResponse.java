package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseResponse<T> {
    public static final Long SUCCESS = 200L;
    private  Long code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T payload;
    public BaseResponse(Long code, T payload){
        this.code = code;
        this.message = message;
    }
    public BaseResponse(Long code, String message){
        this.code = code;
        this.message = message;
    }
    public BaseResponse(Long code){
        this.code = code;
    }
    public BaseResponse(T payload){
        this.code = SUCCESS;
        this.payload = payload;
    }



}

