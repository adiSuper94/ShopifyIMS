package com.adiSuper.shopifyIMS.controller;

import com.adiSuper.shopifyIMS.generated.core.tables.pojos.Supplier;
import com.adiSuper.shopifyIMS.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
public class SupplierController {

    private final SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping("/v1/suppliers/{id}")
    public Supplier getSupplierById(@PathVariable UUID id){
        return supplierService.getSupplierById(id);
    }

    @PatchMapping("/v1/suppliers/{id}")
    public Supplier patchSupplierById(@PathVariable UUID id, @RequestBody Supplier supplier){
        return supplierService.patchSupplierById(id, supplier);
    }

    @DeleteMapping("/v1/suppliers/{id}")
    public int deleteSupplierById(@PathVariable UUID id){
        return supplierService.deleteSupplierById(id);
    }

    @GetMapping("/v1/suppliers/")
    public UUID postSupplier(@RequestBody @Valid Supplier supplier){
        return supplierService.addSupplier(supplier);
    }

}
