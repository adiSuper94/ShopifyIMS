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
        return db.insertInto(Tables.INVENTORY).set(inventoryRecord).returningResult(Tables.INVENTORY.ID).fetchOne(Tables.INVENTORY.ID);
    }

    public int deleteInventoryById(UUID id){
        return db.delete(Tables.INVENTORY).where(Tables.INVENTORY.ID.eq(id)).execute();
    }

    public Optional<Inventory> updateInventoryById(UUID id, Inventory inventory){
        Optional<Inventory> optionalOldInventory = fetchInventoryById(id);
        if(optionalOldInventory.isPresent()){
            inventory.setModifiedAt(LocalDateTime.now());
            inventory.setId(id);
            InventoryRecord inventoryRecord = db.newRecord(Tables.INVENTORY, inventory);
            return Optional.ofNullable(db.update(Tables.INVENTORY).set(inventoryRecord).returningResult(Tables.INVENTORY.fieldsRow()).fetchOneInto(Inventory.class));
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
}
