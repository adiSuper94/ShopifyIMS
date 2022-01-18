package com.adiSuper94.ShopifyIMS.controller;

import com.adiSuper.generated.core.tables.pojos.Catalog;
import com.adiSuper94.ShopifyIMS.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class CatalogController {

    private CatalogService catalogService;

    @Autowired
    public CatalogController(CatalogService service){
        this.catalogService = service;
    }
    @GetMapping("/v1/catalog/{id}")
    public Catalog getCatalogById(@PathVariable UUID id){
        return catalogService.getCatalogById(id);
    }
}
