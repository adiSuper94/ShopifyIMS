package com.adiSuper.shopifyIMS.service;

import com.adiSuper.shopifyIMS.dbAccessor.InventoryDbAccessor;
import com.adiSuper.shopifyIMS.dbAccessor.InventoryHistoryDbAccessor;
import com.adiSuper.shopifyIMS.generated.core.enums.InventoryHistoryType;
import com.adiSuper.shopifyIMS.generated.core.tables.pojos.Inventory;
import com.adiSuper.shopifyIMS.model.InventoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InventoryService {

    private final InventoryDbAccessor inventoryDbAccessor;
    private final InventoryHistoryDbAccessor historyDbAccessor;

    @Autowired
    public InventoryService(InventoryDbAccessor inventoryDbAccessor, InventoryHistoryDbAccessor historyDbAccessor) {
        this.inventoryDbAccessor = inventoryDbAccessor;
        this.historyDbAccessor = historyDbAccessor;
    }

    public Inventory getInventoryById(UUID id){
        Optional<Inventory> optionalInventory = inventoryDbAccessor.fetchInventoryById(id);
        return optionalInventory.orElseThrow(() -> new EntityNotFoundException("Inventory not found with id:: " + id.toString()));
    }

    public Inventory patchInventoryById(UUID id, Inventory inventory){
        Optional<Inventory> optionalInventory = inventoryDbAccessor.updateInventoryById(id, inventory);
        return optionalInventory.orElseThrow(() -> new EntityNotFoundException("Inventory not found with id::" + id.toString() + "(or could not not update row with given values)"));
    }

    public UUID addOrReduceInventory(InventoryRequest inventoryRequest){
        Inventory filter = new Inventory();
        filter.setSku(inventoryRequest.getSku());
        //filter.setSupplierId(inventoryRequest.getSupplierId());
        Inventory inventory;
        try{
            inventory = getAllInventory(Optional.of(filter)).get(0);
        }
        catch (EntityNotFoundException ex){
            inventory = new Inventory();
            inventory.setQuantity(0);
            inventory.setSku(inventoryRequest.getSku());
            //inventory.setSupplierId(inventory.getSupplierId());
        }

        InventoryHistoryType type;
        if(inventoryRequest.isReduce()){
            if(inventory.getQuantity() - inventoryRequest.getCount() < 0){
                throw new DataIntegrityViolationException("Quantity cannot be < 0.");
            }
            inventory.setQuantity(inventory.getQuantity() - inventoryRequest.getCount());
            type = InventoryHistoryType.reduce_inventory;
        }else{
            inventory.setQuantity(inventory.getQuantity() + inventoryRequest.getCount());
            type = InventoryHistoryType.add_inventory;
        }
        return inventoryDbAccessor.upsertInventory(inventory, type);
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

    /* public Inventory addOrReduceInventoryById(UUID id, InventoryRequest inventoryRequest) {
        Inventory inventory = getInventoryById(id);
        if(inventoryRequest.isReduce()){
            inventory.setQuantity(inventory.getQuantity() - inventoryRequest.getCount());
        }else{
            inventory.setQuantity(inventory.getQuantity() + inventoryRequest.getCount());
        }
        return patchInventoryById(id, inventory);

    }*/
}
