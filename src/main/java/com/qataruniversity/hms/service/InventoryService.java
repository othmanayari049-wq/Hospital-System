package com.qataruniversity.hms.service;

import com.qataruniversity.hms.domain.InventoryItem;
import com.qataruniversity.hms.exception.EntityNotFoundException;
import com.qataruniversity.hms.repository.CrudRepository;

import java.util.Comparator;
import java.util.List;

public final class InventoryService {
    private final CrudRepository<InventoryItem> inventory;

    public InventoryService(CrudRepository<InventoryItem> inventory) {
        this.inventory = inventory;
    }

    public InventoryItem add(InventoryItem item) {
        return inventory.save(item);
    }

    public InventoryItem receive(String id, int quantity) {
        InventoryItem item = get(id);
        item.addStock(quantity);
        return inventory.save(item);
    }

    public InventoryItem consume(String id, int quantity) {
        InventoryItem item = get(id);
        item.removeStock(quantity);
        return inventory.save(item);
    }

    public InventoryItem get(String id) {
        return inventory.findById(id).orElseThrow(() -> new EntityNotFoundException("Inventory item", id));
    }

    public List<InventoryItem> list() {
        return inventory.findAll().stream().sorted(Comparator.comparing(InventoryItem::getName)).toList();
    }

    public List<InventoryItem> lowStockItems() {
        return list().stream().filter(InventoryItem::isLowStock).toList();
    }
}
