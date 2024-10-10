package com.microservice.order.common;

import com.microservice.order.dto.OrderDTO;
import lombok.Getter;

@Getter
public class SuccessOrderResponse  implements OrderResponse{
    private final OrderDTO orderDTO;
    public  SuccessOrderResponse(OrderDTO orderDTO){
        this.orderDTO = orderDTO;
    }
}
