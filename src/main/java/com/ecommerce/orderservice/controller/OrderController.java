package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.models.Order;
import com.ecommerce.orderservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public List<Order> findAllOrders() {
        log.info("Running findAllOrders method from OrderController");
        return orderService.getAllOrders();
    }

    @GetMapping("/orders/{orderId}")
    public Optional<Order> findByOrderId(@PathVariable("orderId") Long orderId) throws Exception {
        log.info("Running getOrderById method from OrderController with orderId " + orderId);
        Optional<Order> order = orderService.findByOrderId(orderId);
        return order;
    }

    @PostMapping("/orders/create")
    public Order createOrder(@RequestBody Order order) {
        log.info("Running createOrder method from OrderController with order " + order.getOrderId());
        return orderService.saveOrder(order);
    }


    @GetMapping("/orders/cancel/{orderId}")
    public String cancelOrder(@PathVariable("orderId") Long orderId) throws Exception {
        log.info("Running cancelOrder method from OrderController with orderId " + orderId);

        Optional<Order> orderToBeCancelled = orderService.findByOrderId(orderId);
        if (orderToBeCancelled.isPresent()) {
            orderService.cancelOrder(orderToBeCancelled);
            return "Cancelled Successfully!";
        } else throw new Exception("Order not found with id " + orderId);
    }

    @GetMapping("/orders/delete/{orderId}")
    public String deleteOrder(@PathVariable("orderId") Long orderId) throws Exception {
        log.info("Running deleteOrder method from OrderController for " + orderId);

        Optional<Order> orderToBeDeleted = orderService.findByOrderId(orderId);
        if (orderToBeDeleted.isPresent()) {
            orderService.deleteOrder(orderToBeDeleted);
            return "Delete Successfully!";
        } else throw new Exception("Order not found with id " + orderId);
    }

}
