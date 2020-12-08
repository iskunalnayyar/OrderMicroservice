package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.models.Item;
import com.ecommerce.orderservice.models.Order;
import com.ecommerce.orderservice.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public List<Item> findItemsByOrderId(Long orderId) {
        return itemRepository.findByOrder(orderId);
    }

    public Item addItem(Item item) {
        return itemRepository.save(item);
    }

    public Optional<Item> findById(Long itemId) {
        return itemRepository.findById(itemId);
    }

    public List<Item> getAllItems() {
        return (List<Item>) itemRepository.findAll();
    }

    public void deleteItem(Optional<Item> itemToBeDeleted) {
        itemRepository.delete(itemToBeDeleted.get());
        return;
    }
}
