package com.adiSuper.shopifyIMS.dbAccessor;

import com.adiSuper.shopifyIMS.generated.core.Tables;
import com.adiSuper.shopifyIMS.generated.core.enums.InventoryHistoryType;
import com.adiSuper.shopifyIMS.generated.core.tables.daos.CatalogDao;
import com.adiSuper.shopifyIMS.generated.core.tables.pojos.Catalog;
import com.adiSuper.shopifyIMS.generated.core.tables.records.CatalogRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CatalogDbAccessor {
    private final CatalogDao dao;
    private final DSLContext db;
    private final InventoryHistoryDbAccessor historyDbAccessor;

    @Autowired
    public CatalogDbAccessor(CatalogDao dao, DSLContext db, InventoryHistoryDbAccessor historyDbAccessor) {
        this.dao = dao;
        this.db = db;
        this.historyDbAccessor = historyDbAccessor;
    }

    public Optional<Catalog> fetchCatalogById(UUID id) {
        return dao.fetchOptional(Tables.CATALOG.ID, id);
    }

    public Optional<Catalog> getCatalogBySku(String sku) {
        return dao.fetchOptional(Tables.CATALOG.SKU, sku);
    }

    public UUID insertCatalog(Catalog catalog) {
        CatalogRecord catalogRecord = db.newRecord(Tables.CATALOG, catalog);
        LocalDateTime now = LocalDateTime.now();
        catalogRecord.setCreatedAt(now);
        catalogRecord.setModifiedAt(now);
        final UUID[] insertedCatalogId = new UUID[1];
        db.transaction(configuration -> {
            insertedCatalogId[0] = db.insertInto(Tables.CATALOG).set(catalogRecord).returningResult(Tables.CATALOG.ID).fetchOne(Tables.CATALOG.ID);
            historyDbAccessor.insertHistoryRecord(catalog.getSku(), 0, InventoryHistoryType.create_catalog_entry);
        });
        return insertedCatalogId[0];
    }

    public Optional<Catalog> updateCatalogById(UUID id, Catalog catalog) {
        Optional<Catalog> optionalOldCatalog = this.fetchCatalogById(id);
        if (optionalOldCatalog.isPresent()) {
            LocalDateTime now = LocalDateTime.now();
            catalog.setId(id);
            catalog.setModifiedAt(now);
            CatalogRecord catalogRecord = db.newRecord(Tables.CATALOG, catalog);
            String sku = catalog.getSku() == null? optionalOldCatalog.get().getSku(): catalog.getSku();
            final Catalog[] updatedCatalog = new Catalog[1];
            db.transaction(configuration -> {
                updatedCatalog[0] = db.update(Tables.CATALOG).set(catalogRecord).returningResult(Tables.CATALOG.fieldsRow()).fetchOneInto(Catalog.class);
                historyDbAccessor.insertHistoryRecord(sku, 0, InventoryHistoryType.edit_catalog_entry);
            });
            return Optional.ofNullable(updatedCatalog[0]);
        }
        return Optional.empty();
    }

    public int deleteCatalogById(UUID id) {
        Optional<Catalog> optionalCatalog = fetchCatalogById(id);
        if(optionalCatalog.isPresent()){
            Catalog catalog = optionalCatalog.get();
            int[] deleteCount = new int[1];
            db.transaction(configuration -> {
                historyDbAccessor.insertHistoryRecord(catalog.getSku(), 0, InventoryHistoryType.delete_catalog_entry);
                deleteCount[0] = db.delete(Tables.CATALOG).where(Tables.CATALOG.ID.eq(id)).execute();
            });

            return deleteCount[0];
        }
        throw new EntityNotFoundException("No Catalog present with id:: " + id.toString() + " to delete.");
    }

    public List<Catalog> getAllCatalog(Optional<Catalog> filter) {
        if(filter.isEmpty()){
            return fetchWithCondition(DSL.trueCondition());
        }
        Condition condition = andAllConditions(filter.get());
        return fetchWithCondition(condition);
    }

    private Condition andAllConditions(Catalog catalog) {
        Condition condition = DSL.trueCondition();
        if(catalog.getName() != null){
            condition = condition.and(Tables.CATALOG.NAME.eq(catalog.getName()));
        }
        if(catalog.getSku() != null){
            condition = condition.and(Tables.CATALOG.SKU.eq(catalog.getSku()));
        }
        if(catalog.getId() != null){
            condition = condition.and(Tables.CATALOG.ID.eq(catalog.getId()));
        }
        if(catalog.getPrice() != null){
            condition = condition.and(Tables.CATALOG.PRICE.eq(catalog.getPrice()));
        }
        return condition;
    }

    private List<Catalog> fetchWithCondition(Condition condition){
        return db.selectFrom(Tables.CATALOG).where(condition).fetchInto(Catalog.class);
    }
}
