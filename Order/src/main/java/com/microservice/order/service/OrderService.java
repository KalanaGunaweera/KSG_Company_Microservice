package com.microservice.order.service;

import com.microservice.order.dto.OrderDTO;
import com.microservice.order.module.Order;
import com.microservice.order.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<OrderDTO> getAllOrders() {
        List<Order> orderList = orderRepository.findAll();
        return modelMapper.map(orderList,new TypeToken<List<OrderDTO>>(){}.getType());
        //This method retrieves a list of User entities from the repository and converts it into a list of UserDto objects using ModelMapper.

    }

    public OrderDTO saveOrder(OrderDTO orderDTO ) {
        orderRepository.save(modelMapper.map(orderDTO, Order.class));
        return orderDTO;
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
