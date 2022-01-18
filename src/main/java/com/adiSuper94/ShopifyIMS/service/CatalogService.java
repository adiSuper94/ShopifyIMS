package com.adiSuper94.ShopifyIMS.service;

import com.adiSuper.generated.core.tables.pojos.Catalog;
import com.adiSuper94.ShopifyIMS.dbAccessor.CatalogDbAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Service
public class CatalogService {

    private CatalogDbAccessor dbAccessor;

    @Autowired
    public CatalogService(CatalogDbAccessor catalogDbAccessor){
        this.dbAccessor = catalogDbAccessor;
    }

    public Catalog getCatalogById(UUID id){
        Optional<Catalog> optionalCatalog = dbAccessor.findCatalogById(id);
        return optionalCatalog.orElseThrow(() -> new EntityNotFoundException("Catalog not found with id::" + id.toString()));
    }
}
