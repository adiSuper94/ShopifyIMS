package com.adiSuper.shopifyIMS.controller;

import com.adiSuper.shopifyIMS.generated.core.tables.pojos.Inventory;
import com.adiSuper.shopifyIMS.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/v1/inventories/{id}")
    public Inventory getInventoryById(@PathVariable UUID id){
        return inventoryService.getInventoryById(id);
    }

    @PatchMapping("/v1/inventories/{id}")
    public Inventory patchInventoryById(@PathVariable UUID id, @RequestBody Inventory inventory){
        return inventoryService.patchInventoryById(id, inventory);
    }

    @DeleteMapping("/v1/inventories/{id}")
    public int deleteInventoryById(@PathVariable UUID id){
        return inventoryService.deleteInventoryById(id);
    }

    @GetMapping("/v1/inventories")
    public List<Inventory> getAllInventory(@RequestBody Optional<Inventory> filter){
        return inventoryService.getAllInventory(filter);
    }

    @PostMapping("/v1/inventories")
    public UUID postInventory(@RequestBody @Valid Inventory inventory){
        return inventoryService.createInventory(inventory);
    }
}
