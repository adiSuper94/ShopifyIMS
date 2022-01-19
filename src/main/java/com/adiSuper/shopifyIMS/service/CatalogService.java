package com.adiSuper.shopifyIMS.service;

import com.adiSuper.shopifyIMS.dbAccessor.CatalogDbAccessor;
import com.adiSuper.shopifyIMS.generated.core.Tables;
import com.adiSuper.shopifyIMS.generated.core.tables.daos.CatalogDao;
import com.adiSuper.shopifyIMS.generated.core.tables.pojos.Catalog;
import com.adiSuper.shopifyIMS.generated.core.tables.records.CatalogRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CatalogService {

    private final CatalogDbAccessor catalogDbAccessor;

    @Autowired
    public CatalogService(CatalogDbAccessor catalogDbAccessor){
        this.catalogDbAccessor = catalogDbAccessor;
    }

    public Catalog getCatalogById(UUID id){
        Optional<Catalog> optionalCatalog = catalogDbAccessor.fetchCatalogById(id);
        return optionalCatalog.orElseThrow(() -> new EntityNotFoundException("Catalog not found with id::" + id.toString()));
    }

    public List<Catalog> getAllCatalog(Optional<Catalog> filter){
        List<Catalog> catalogs = catalogDbAccessor.getAllCatalog(filter);
        if(catalogs.isEmpty()){
            throw new EntityNotFoundException("No Catalog found.");
        }
        return catalogs;
    }

    public UUID addCatalog(Catalog catalog){
        return catalogDbAccessor.insertCatalog(catalog);
    }

    public Catalog patchCatalogById(UUID id, Catalog catalog) {
        Optional<Catalog> optionalCatalog = catalogDbAccessor.updateCatalogById(id, catalog);
        return optionalCatalog.orElseThrow(() -> new EntityNotFoundException("Catalog not found with id::" + id.toString() + "(or could not not update row with given values)"));
    }

    public int deleteCatalogById(UUID id){
        int deletedRowCount = catalogDbAccessor.deleteCatalogById(id);
        if(deletedRowCount == 0){
            throw new EntityNotFoundException("Catalog not found with id::" + id.toString() + " so delete statement not executed");
        }
        return deletedRowCount;
    }

}
