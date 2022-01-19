package com.adiSuper.shopifyIMS.controller;

import com.adiSuper.shopifyIMS.service.CatalogService;
import com.adiSuper.shopifyIMS.generated.core.tables.pojos.Catalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
public class CatalogController {

    private final CatalogService catalogService;

    @Autowired
    public CatalogController(CatalogService service){
        this.catalogService = service;
    }

    @GetMapping("/v1/catalogs/{id}")
    public Catalog getCatalogById(@PathVariable UUID id){
        return catalogService.getCatalogById(id);
    }

    @PatchMapping("/v1/catalogs/{id}")
    public Catalog patchCatalogById(@RequestBody Catalog catalog, @PathVariable UUID id){
        return catalogService.patchCatalogById(id, catalog);
    }

    @DeleteMapping("/v1/catalogs/{id}")
    public int deleteCatalog(@PathVariable UUID id){
        return catalogService.deleteCatalogById(id);
    }

    @GetMapping("/v1/catalogs/")
    public Catalog getCatalogById(@RequestParam String sku){
        return catalogService.getCatalogBySku(sku);
    }

    @PostMapping("/v1/catalogs/")
    public UUID postCatalog(@RequestBody @Valid Catalog catalog){
        return catalogService.addCatalog(catalog);
    }

}
