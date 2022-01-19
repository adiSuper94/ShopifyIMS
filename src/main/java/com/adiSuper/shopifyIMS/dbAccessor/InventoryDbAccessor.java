package com.adiSuper.shopifyIMS.dbAccessor;

import com.adiSuper.shopifyIMS.generated.core.Tables;
import com.adiSuper.shopifyIMS.generated.core.enums.InventoryHistoryType;
import com.adiSuper.shopifyIMS.generated.core.tables.daos.InventoryDao;
import com.adiSuper.shopifyIMS.generated.core.tables.pojos.Inventory;
import com.adiSuper.shopifyIMS.generated.core.tables.records.InventoryRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.jooq.impl.DSL.trueCondition;

@Repository
public class InventoryDbAccessor {

    private final DSLContext db;
    private final InventoryDao dao;
    private final InventoryHistoryDbAccessor historyDbAccessor;

    @Autowired
    public InventoryDbAccessor(DSLContext db, InventoryDao dao, InventoryHistoryDbAccessor historyDbAccessor) {
        this.db = db;
        this.dao = dao;
        this.historyDbAccessor = historyDbAccessor;
    }

    public Optional<Inventory> fetchInventoryById(UUID id){
        return dao.fetchOptional(Tables.INVENTORY.ID, id);
    }

    public UUID insertInventory(Inventory inventory){
        LocalDateTime now = LocalDateTime.now();
        inventory.setCreatedAt(now);
        inventory.setModifiedAt(now);
        InventoryRecord inventoryRecord = db.newRecord(Tables.INVENTORY, inventory);
        UUID[] ids = new UUID[1];
        db.transaction(configuration -> {
            ids[0] = db.insertInto(Tables.INVENTORY).set(inventoryRecord).returningResult(Tables.INVENTORY.ID).fetchOne(Tables.INVENTORY.ID);
            historyDbAccessor.insertHistoryRecord(inventory.getSku(), inventory.getQuantity(), InventoryHistoryType.create_inventory_entry);
        });
        return ids[0];
    }

    public int deleteInventoryById(UUID id){
        Optional<Inventory> optionalInventory = fetchInventoryById(id);
        if(optionalInventory.isPresent()){
            Inventory inventory = optionalInventory.get();
            int[] deleteCount = new int[1];
            db.transaction(configuration -> {
                historyDbAccessor.insertHistoryRecord(inventory.getSku(), 0, InventoryHistoryType.delete_inventory_entry);
                deleteCount[0] = db.delete(Tables.INVENTORY).where(Tables.INVENTORY.ID.eq(id)).execute();
            });
            return deleteCount[0];
        }
        throw new EntityNotFoundException("No Inventory present with id:: " + id.toString() + " to delete.");
    }

    public Optional<Inventory> updateInventoryById(UUID id, Inventory inventory){
        Optional<Inventory> optionalOldInventory = fetchInventoryById(id);
        if(optionalOldInventory.isPresent()){
            inventory.setModifiedAt(LocalDateTime.now());
            inventory.setId(id);
            InventoryRecord inventoryRecord = db.newRecord(Tables.INVENTORY, inventory);
            String sku = inventory.getSku() == null ? optionalOldInventory.get().getSku(): inventory.getSku();
            int quantity = inventory.getQuantity() == null ? optionalOldInventory.get().getQuantity(): inventory.getQuantity();
            Inventory[] inventories = new Inventory[1];
            db.transaction(configuration -> {
                historyDbAccessor.insertHistoryRecord(sku, quantity, InventoryHistoryType.edit_inventory_entry);
                inventories[0] = db.update(Tables.INVENTORY).set(inventoryRecord).returningResult(Tables.INVENTORY.fieldsRow()).fetchOneInto(Inventory.class);
            });
            return Optional.ofNullable(inventories[0]);
        }
        return Optional.empty();
    }

    public List<Inventory> fetchAllInventories(Optional<Inventory> filter){
        if(filter.isEmpty()){
            return fetchWithCondition(trueCondition());
        }
        return filterAndFetch(filter.get());
    }


    private List<Inventory> filterAndFetch(Inventory inventory) {
        Condition condition = andAllCriteria(inventory);
        return fetchWithCondition(condition);
    }

    private Condition andAllCriteria(Inventory inventory) {
        Condition condition= trueCondition();
        if(inventory.getId() != null){
            condition = condition.and(Tables.INVENTORY.ID.eq(inventory.getId()));
        }
        if(inventory.getSupplierId()!= null){
            condition = condition.and(Tables.INVENTORY.SUPPLIER_ID.eq(inventory.getSupplierId()));
        }
        if(inventory.getSku() != null){
            condition = condition.and(Tables.INVENTORY.SKU.eq(inventory.getSku()));
        }
        return condition;
    }

    private List<Inventory> fetchWithCondition(Condition condition){
        return db.selectFrom(Tables.INVENTORY).where(condition).fetch().into(Inventory.class);
    }

    public UUID upsertInventory(Inventory inventory, InventoryHistoryType type) {
        LocalDateTime now = LocalDateTime.now();

        if(inventory.getId() == null){
            inventory.setCreatedAt(now);
        }
        inventory.setModifiedAt(now);
        InventoryRecord inventoryRecord = db.newRecord(Tables.INVENTORY, inventory);

        db.transaction(configuration -> {
            inventoryRecord.insert();
            historyDbAccessor.insertHistoryRecord(inventory.getSku(), inventory.getQuantity(), type);
        });
        return inventoryRecord.getId();
    }
}
