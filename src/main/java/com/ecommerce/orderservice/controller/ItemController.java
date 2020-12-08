package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.models.Item;
import com.ecommerce.orderservice.models.Order;
import com.ecommerce.orderservice.repository.OrderRepository;
import com.ecommerce.orderservice.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class ItemController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemService itemService;

    @GetMapping("/items/{orderId}")
    public List<Item> getItemsByOrderId(@PathVariable("orderId") Long orderId) {
        log.info("Inside getItemsByOrderId method of ItemController");
        return itemService.findItemsByOrderId(orderId);
    }

    @Transactional
    @PostMapping("/items/add/{orderId}")
    public Item addItemToOrder(@PathVariable("orderId") Long orderId, @RequestBody Item item) {
        log.info("Inside addItem method of ItemController");
        Optional<Order> order = orderRepository.findById(orderId);
        item.setOrder(order.get());
        return itemService.addItem(item);
    }


    @GetMapping("/items/search/{itemId}")
    public Item getItem(@PathVariable("itemId") Long itemId) throws Exception {
        log.info("Inside getItem method of ItemController");

        Optional<Item> optionalItem = itemService.findById(itemId);
        if (optionalItem.isPresent()) {
            return optionalItem.get();
        } else {
            throw new Exception("Item not found with id " + itemId);
        }
    }

    @GetMapping("/items")
    public List<Item> findAllItems() {
        log.info("Inside findAllItems method of ItemController");
        return itemService.getAllItems();
    }

    @GetMapping("/items/delete/{itemId}")
    public String deleteItem(@PathVariable("itemId") Long itemId) throws Exception {
        log.info("Inside deleteOrder method of ItemController");

        Optional<Item> itemToBeDeleted = itemService.findById(itemId);
        if (itemToBeDeleted.isPresent()) {
            itemService.deleteItem(itemToBeDeleted);
            return "Delete Successfully!";
        } else throw new Exception("Item not found with id " + itemId);
    }

}
