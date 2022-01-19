package com.adiSuper.shopifyIMS.dbAccessor;

import com.adiSuper.shopifyIMS.generated.core.Tables;
import com.adiSuper.shopifyIMS.generated.core.tables.daos.CatalogDao;
import com.adiSuper.shopifyIMS.generated.core.tables.pojos.Catalog;
import com.adiSuper.shopifyIMS.generated.core.tables.records.CatalogRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CatalogDbAccessor {
    private final CatalogDao dao;
    private final DSLContext db;

    @Autowired
    public CatalogDbAccessor(CatalogDao dao, DSLContext db) {
        this.dao = dao;
        this.db = db;
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
        return db.insertInto(Tables.CATALOG).set(catalogRecord).returningResult(Tables.CATALOG.ID).fetchOne(Tables.CATALOG.ID);
    }

    public Optional<Catalog> updateCatalogById(UUID id, Catalog catalog) {
        Optional<Catalog> optionalOldCatalog = this.fetchCatalogById(id);
        if (optionalOldCatalog.isPresent()) {
            catalog.setId(id);
            catalog.setModifiedAt(LocalDateTime.now());
            CatalogRecord catalogRecord = db.newRecord(Tables.CATALOG, catalog);
            return Optional.ofNullable(db.update(Tables.CATALOG).set(catalogRecord).returningResult(Tables.CATALOG.fieldsRow()).fetchOneInto(Catalog.class));
        }
        return Optional.empty();
    }

    public int deleteCatalogById(UUID id) {
        return db.delete(Tables.CATALOG).where(Tables.CATALOG.ID.eq(id)).execute();
    }
}
