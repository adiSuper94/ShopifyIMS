package com.adiSuper.shopifyIMS.service;

import com.adiSuper.shopifyIMS.generated.core.tables.pojos.Catalog;
import com.adiSuper.shopifyIMS.generated.core.tables.pojos.Inventory;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class CSVGeneratorService {

    private final InventoryService inventoryService;
    private final CatalogService catalogService;

    @Autowired
    public CSVGeneratorService(InventoryService inventoryService, CatalogService catalogService) {
        this.inventoryService = inventoryService;
        this.catalogService = catalogService;
    }

    public Path generateCatalogCSVReport() throws IOException {
        List<Catalog> catalogList = catalogService.getAllCatalog(Optional.empty());
        String catalogCsv = catalogToCsv(catalogList);
        return writeStringToFile(catalogCsv, "src/main/resources/reports/catalog-report.csv");
    }

    public Path generateInventoryCSVReport() throws IOException {
        List<Inventory> inventoryList = inventoryService.getAllInventory(Optional.empty());
        String inventoryCsv = inventoryToCsv(inventoryList);
        return writeStringToFile(inventoryCsv, "src/main/resources/reports/inventory-report.csv");
    }

    private String inventoryToCsv(List<Inventory> inventoryList){
        StringBuilder sb = new StringBuilder();
        sb.append("inventory_id, sku, quantity, created_at, modified_at\n");
        for(Inventory inventory : inventoryList){
            sb.append(inventory.getId());
            sb.append(", ").append(inventory.getSku());
            sb.append(", ").append(inventory.getQuantity());
            sb.append(", ").append(inventory.getCreatedAt());
            sb.append(", ").append(inventory.getModifiedAt());
            sb.append("\n");
        }
        return sb.toString();
    }

    private String catalogToCsv(List<Catalog> catalogList){
        StringBuilder sb = new StringBuilder();
        sb.append("catalog_id, sku, name, details, price, created_at, modified_at\n");
        for(Catalog catalog : catalogList){
            sb.append(catalog.getId());
            sb.append(", ").append(catalog.getSku());
            sb.append(", ").append(catalog.getName());
            sb.append(", ").append(catalog.getDetails());
            sb.append(", ").append(catalog.getPrice());
            sb.append(", ").append(catalog.getCreatedAt());
            sb.append(", ").append(catalog.getModifiedAt());
            sb.append("\n");
        }
        return sb.toString();
    }

    private Path writeStringToFile(String text, String filename) throws IOException {
        byte[] buffer = text.getBytes(StandardCharsets.UTF_8);
        File targetFile = new File(filename);
        OutputStream outStream = new FileOutputStream(targetFile);
        outStream.write(buffer);
        IOUtils.closeQuietly(outStream);
        return Paths.get(targetFile.getAbsolutePath());
    }
}
