package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.models.Order;
import com.ecommerce.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getAllOrders() {
        log.info("Inside of getAllOrders of OrderService");
        return (List<Order>) orderRepository.findAll();
    }

    public Optional<Order> findByOrderId(Long orderId) {
        log.info("Inside getOrderById of OrderService");
        return orderRepository.findById(orderId);
    }

    public Order findByCustomerName(String customerName) {
        log.info("Inside getOrderById of OrderService");
        return orderRepository.findByCustomerName(customerName);
    }
    public Order saveOrder(Order order) {
        log.info("Inside createOrder of OrderService");
        setupOrder(order);
        return orderRepository.save(order);
    }

    public void deleteOrder(Optional<Order> order) {
        log.info("Inside createOrder of OrderService");
        orderRepository.delete(order.get());
        return;
    }

    private Order setupOrder(Order order) {
        Order saveOrder = order;
        saveOrder.setCreatedDate(Date.from(Instant.now()));
        saveOrder.setStatus("RECEIVED");
        return order;
    }

    public void cancelOrder(Optional<Order> orderToBeCancelled) {
        log.info("Inside cancelOrder of OrderService");
        orderToBeCancelled.get().setStatus("Cancelled");
        orderRepository.save(orderToBeCancelled.get());
    }
}
