package com.adiSuper.shopifyIMS.model;


import java.util.UUID;

public class InventoryRequest {

    private final boolean isReduce;
    private final int count;
    private final String sku;
    private final UUID supplierId;

    public InventoryRequest(boolean isReduce, int count, String sku, UUID supplierId) {
        this.isReduce = isReduce;
        this.count = count;
        this.sku = sku;
        this.supplierId = supplierId;
    }

    public boolean isReduce() {
        return isReduce;
    }

    public int getCount() {
        return count;
    }

    public String getSku() {
        return sku;
    }

    public UUID getSupplierId() {
        return supplierId;
    }
}
