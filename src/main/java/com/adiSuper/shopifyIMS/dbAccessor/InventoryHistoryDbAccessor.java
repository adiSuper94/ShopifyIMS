package com.adiSuper.shopifyIMS.dbAccessor;

import com.adiSuper.shopifyIMS.generated.core.Tables;
import com.adiSuper.shopifyIMS.generated.core.enums.InventoryHistoryType;
import com.adiSuper.shopifyIMS.generated.core.tables.pojos.InventoryHistory;
import com.adiSuper.shopifyIMS.generated.core.tables.records.InventoryHistoryRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public class InventoryHistoryDbAccessor {

    private final DSLContext db;

    @Autowired
    public InventoryHistoryDbAccessor(DSLContext db) {
        this.db = db;
    }

    public UUID insertInventoryHistory(InventoryHistory inventoryHistory){
        LocalDateTime now = LocalDateTime.now();
        inventoryHistory.setCreatedAt(now);
        inventoryHistory.setModifiedAt(now);
        InventoryHistoryRecord record = db.newRecord(Tables.INVENTORY_HISTORY, inventoryHistory);
        return db.insertInto(Tables.INVENTORY_HISTORY).set(record).returningResult(Tables.INVENTORY_HISTORY.ID).fetchOne(Tables.INVENTORY_HISTORY.ID);
    }

    public UUID insertHistoryRecord(String sku, int quantity, InventoryHistoryType type){
        LocalDateTime now = LocalDateTime.now();
        quantity = Math.max(quantity, 0);
        InventoryHistoryRecord historyRecord = db.newRecord(Tables.INVENTORY_HISTORY);
        historyRecord.setModifiedAt(now);
        historyRecord.setModifiedAt(now);
        historyRecord.setQuantity(quantity);
        historyRecord.setSku(sku);
        historyRecord.setType(type);
        return db.insertInto(Tables.INVENTORY_HISTORY).set(historyRecord).returningResult(Tables.INVENTORY_HISTORY.ID).fetchOne(Tables.INVENTORY_HISTORY.ID);
    }
}
