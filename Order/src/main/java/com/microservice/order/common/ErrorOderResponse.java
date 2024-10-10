package com.microservice.order.common;

import lombok.Getter;

@Getter
public class ErrorOderResponse implements OrderResponse{
    private final String errorMessage;
    public ErrorOderResponse(String errorMessage){
        this.errorMessage = errorMessage;
    }
}
