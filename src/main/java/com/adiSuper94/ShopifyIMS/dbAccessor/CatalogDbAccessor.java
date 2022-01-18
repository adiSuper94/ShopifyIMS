package com.adiSuper94.ShopifyIMS.dbAccessor;

import com.adiSuper.generated.core.tables.pojos.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

public interface CatalogDbAccessor extends JpaRepository<Catalog, UUID> {

    public Optional<Catalog> findCatalogById(UUID id);
    public Optional<Catalog> findCatalogBySku(String sku);
}
