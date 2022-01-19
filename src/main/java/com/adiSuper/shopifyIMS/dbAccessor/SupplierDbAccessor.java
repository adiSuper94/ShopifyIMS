package com.adiSuper.shopifyIMS.dbAccessor;

import com.adiSuper.shopifyIMS.generated.core.Tables;
import com.adiSuper.shopifyIMS.generated.core.tables.daos.SupplierDao;
import com.adiSuper.shopifyIMS.generated.core.tables.pojos.Supplier;
import com.adiSuper.shopifyIMS.generated.core.tables.records.SupplierRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SupplierDbAccessor {

    private final DSLContext db;
    private final SupplierDao dao;

    @Autowired
    public SupplierDbAccessor(DSLContext db, SupplierDao dao) {
        this.db = db;
        this.dao = dao;
    }

    public Optional<Supplier> fetchSupplierById(UUID id){
        return dao.fetchOptional(Tables.SUPPLIER.ID, id);
    }

    public Optional<Supplier> updateSupplierById(UUID id, Supplier supplier) {
        Optional<Supplier> optionalOldSupplier = this.fetchSupplierById(id);
        if (optionalOldSupplier.isPresent()) {
            supplier.setModifiedAt(LocalDateTime.now());
            supplier.setId(id);
            SupplierRecord supplierRecord = db.newRecord(Tables.SUPPLIER, supplier);
            supplierRecord.update();
            supplierRecord.refresh();
            return Optional.ofNullable(supplierRecord.into(Supplier.class));
        }
        return Optional.empty();
    }

    public int deleteSupplierById(UUID id) {
        return db.delete(Tables.SUPPLIER).where(Tables.SUPPLIER.ID.eq(id)).execute();
    }

    public UUID insertSupplier(Supplier supplier){
        LocalDateTime now = LocalDateTime.now();
        supplier.setCreatedAt(now);
        supplier.setModifiedAt(now);
        SupplierRecord supplierRecord = db.newRecord(Tables.SUPPLIER, supplier);
        return db.insertInto(Tables.SUPPLIER).set(supplierRecord).returningResult(Tables.SUPPLIER.ID).fetchOne(Tables.SUPPLIER.ID);
    }
}
