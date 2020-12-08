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
        log.info("Inside findAllOrders method of OrderController");
        return orderService.getAllOrders();
    }

    @GetMapping("/orders/{orderId}")
    public Optional<Order> findByOrderId(@PathVariable("orderId") Long orderId) throws Exception {
        log.info("Inside getOrderById method of OrderController");
        Optional<Order> order = orderService.findByOrderId(orderId);
        return order;
    }

    @PostMapping("/orders/create")
    public Order createOrder(@RequestBody Order order) {
        log.info("Inside createOrder method of OrderController");
        return orderService.saveOrder(order);
    }


    @GetMapping("/orders/cancel/{orderId}")
    public String cancelOrder(@PathVariable("orderId") Long orderId) throws Exception {
        log.info("Inside cancelOrder method of OrderController");

        Optional<Order> orderToBeCancelled = orderService.findByOrderId(orderId);
        if (orderToBeCancelled.isPresent()) {
            orderService.cancelOrder(orderToBeCancelled);
            return "Cancelled Successfully!";
        } else throw new Exception("Order not found with id " + orderId);
    }

    @GetMapping("/orders/delete/{orderId}")
    public String deleteOrder(@PathVariable("orderId") Long orderId) throws Exception {
        log.info("Inside deleteOrder method of OrderController");

        Optional<Order> orderToBeDeleted = orderService.findByOrderId(orderId);
        if (orderToBeDeleted.isPresent()) {
            orderService.deleteOrder(orderToBeDeleted);
            return "Delete Successfully!";
        } else throw new Exception("Order not found with id " + orderId);
    }

}
