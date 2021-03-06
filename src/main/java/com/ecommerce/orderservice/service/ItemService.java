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
        log.info("Running findItemsByOrderId from ItemService with orderId " + orderId);
        return itemRepository.findByOrder(orderId);
    }

    public Item addItem(Item item) {
        log.info("Runing addItem from ItemService");
        return itemRepository.save(item);
    }

    public Optional<Item> findById(Long itemId) {
        log.info("Running findById from ItemService with itemId " + itemId);
        return itemRepository.findById(itemId);
    }

    public List<Item> getAllItems() {
        log.info("Runing getAllItems of ItemService");
        return (List<Item>) itemRepository.findAll();
    }

    public void deleteItem(Optional<Item> itemToBeDeleted) {
        log.info("Running deleteItem of ItemService with itemToBeDeleted " + itemToBeDeleted.get().getItemId());
        itemRepository.delete(itemToBeDeleted.get());
        return;
    }
}
