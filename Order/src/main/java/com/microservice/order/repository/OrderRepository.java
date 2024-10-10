package com.microservice.order.repository;

import com.microservice.order.module.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query(value = "SELECT * FROM order WHERE orderId = ?1", nativeQuery = true)
    Order getOrderById(Integer orderId);

}
