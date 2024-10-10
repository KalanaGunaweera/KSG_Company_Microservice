package com.microservice.order.service;

import com.microservice.inventory.DTO.InventoryDTO;
import com.microservice.order.common.ErrorOderResponse;
import com.microservice.order.common.OrderResponse;
import com.microservice.order.common.SuccessOrderResponse;
import com.microservice.order.dto.OrderDTO;
import com.microservice.order.module.Order;
import com.microservice.order.repository.OrderRepository;
import com.microservice.product.DTO.ProductDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final WebClient inventoryWebClient;
    private final WebClient productWebClient;

    public OrderService( OrderRepository orderRepository, ModelMapper modelMapper, WebClient inventoryWebClient, WebClient productWebClient) {
        this.inventoryWebClient = inventoryWebClient;
        this.productWebClient = productWebClient;
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }


    public List<OrderDTO> getAllOrders() {
        List<Order> orderList = orderRepository.findAll();
        return modelMapper.map(orderList,new TypeToken<List<OrderDTO>>(){}.getType());
        //This method retrieves a list of User entities from the repository and converts it into a list of UserDto objects using ModelMapper.

    }

    public OrderResponse saveOrder(OrderDTO orderDTO ) {
        Integer itemId = orderDTO.getItemId();
        try {
            InventoryDTO inventoryResponse = inventoryWebClient.get()
                    .uri(UriBuilder-> UriBuilder.path("/item/{itemId}").build(itemId))
                    .retrieve()
                    .bodyToFlux(InventoryDTO.class)
                    .blockFirst();

            assert inventoryResponse != null;

            Integer productId = inventoryResponse.getProductId();
            ProductDTO productResponse = productWebClient.get()
                    .uri(UriBuilder-> UriBuilder.path("/item/{itemId}").build(productId))
                    .retrieve()
                    .bodyToFlux(ProductDTO.class)
                    .blockFirst();

            assert productResponse != null;

            if(inventoryResponse.getQuantity()>0){
                if(productResponse.getForSale() == 1){
                    orderRepository.save(modelMapper.map(orderDTO, Order.class));
                }else {
                    return new ErrorOderResponse("This item is not for sale");
                }
                return new SuccessOrderResponse(orderDTO);
            }else {
                return new ErrorOderResponse("This item is not available");
            }
        }catch (WebClientResponseException e) {
            if(e.getStatusCode().is5xxServerError()){
               return new ErrorOderResponse("An error occurred while processing the request");
            }
        }
        return null;
        //This method saves a UserDto by mapping it to a User entity, storing it in the repository, and then returning the original UserDto object.
    }

    public String deleteOrder(int id) {
//        userRepo.delete(userRepo.getReferenceById(id));
        orderRepository.deleteById(id);
        return "User Deleted";
    }
    public String getOrderById(int id) {
        orderRepository.getReferenceById(id);
//        userRepo.findById(id).orElse(null);
        return orderRepository.getReferenceById(id).toString();
    }

    public OrderDTO updateOrder(OrderDTO orderDTO) {
        orderRepository.save(modelMapper.map(orderDTO, Order.class));
        return orderDTO;
    }
}
