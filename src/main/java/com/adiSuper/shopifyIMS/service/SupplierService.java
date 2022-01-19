package com.adiSuper.shopifyIMS.service;

import com.adiSuper.shopifyIMS.dbAccessor.SupplierDbAccessor;
import com.adiSuper.shopifyIMS.generated.core.tables.pojos.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Service
public class SupplierService {

    private final SupplierDbAccessor supplierDbAccessor;

    @Autowired
    public SupplierService(SupplierDbAccessor supplierDbAccessor) {
        this.supplierDbAccessor = supplierDbAccessor;
    }

    public Supplier getSupplierById(UUID id){
        Optional<Supplier> optionalSupplier = supplierDbAccessor.fetchSupplierById(id);
        return optionalSupplier.orElseThrow(() -> new EntityNotFoundException("Supplier not found with id::" + id.toString()));
    }

    public Supplier patchSupplierById(UUID id, Supplier supplier){
        Optional<Supplier> optionalSupplier = supplierDbAccessor.updateSupplierById(id, supplier);
        return optionalSupplier.orElseThrow(() -> new EntityNotFoundException("Supplier not found with id::" + id.toString() + "(or could not not update row with given values)"));
    }

    public int deleteSupplierById(UUID id){
        int deletedRowCount = supplierDbAccessor.deleteSupplierById(id);
        if(deletedRowCount == 0){
            throw new EntityNotFoundException("Supplier not found with id::" + id.toString() + " so delete statement not executed");
        }
        return deletedRowCount;
    }

    public UUID addSupplier(Supplier supplier){
        return supplierDbAccessor.insertSupplier(supplier);
    }
}
