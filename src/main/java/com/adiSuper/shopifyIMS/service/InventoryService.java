package com.adiSuper.shopifyIMS.service;

import com.adiSuper.shopifyIMS.dbAccessor.InventoryDbAccessor;
import com.adiSuper.shopifyIMS.generated.core.tables.pojos.Inventory;
import com.adiSuper.shopifyIMS.generated.core.tables.pojos.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InventoryService {

    private final InventoryDbAccessor inventoryDbAccessor;

    @Autowired
    public InventoryService(InventoryDbAccessor inventoryDbAccessor) {
        this.inventoryDbAccessor = inventoryDbAccessor;
    }

    public Inventory getInventoryById(UUID id){
        Optional<Inventory> optionalInventory = inventoryDbAccessor.fetchInventoryById(id);
        return optionalInventory.orElseThrow(() -> new EntityNotFoundException("Inventory not found with id:: " + id.toString()));
    }

    public Inventory patchInventoryById(UUID id, Inventory inventory){
        Optional<Inventory> optionalSupplier = inventoryDbAccessor.updateInventoryById(id, inventory);
        return optionalSupplier.orElseThrow(() -> new EntityNotFoundException("Inventory not found with id::" + id.toString() + "(or could not not update row with given values)"));
    }

    public UUID createInventory(Inventory inventory){
        return inventoryDbAccessor.insertInventory(inventory);
    }

    public int deleteInventoryById(UUID id){
        int deletedRowCount =  inventoryDbAccessor.deleteInventoryById(id);
        if(deletedRowCount == 0){
            throw new EntityNotFoundException("Inventory not found with id::" + id.toString() + " so delete statement not executed");
        }
        return deletedRowCount;
    }

    public List<Inventory> getAllInventory(Optional<Inventory> filter){
        List<Inventory> inventories =  inventoryDbAccessor.fetchAllInventories(filter);
        if(inventories.isEmpty()){
            throw new EntityNotFoundException("No Inventory found.");
        }
        return inventories;
    }
}
