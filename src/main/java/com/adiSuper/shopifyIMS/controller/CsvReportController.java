package com.adiSuper.shopifyIMS.controller;

import com.adiSuper.shopifyIMS.service.CSVGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
public class CsvReportController {

    private final CSVGeneratorService csvGeneratorService;

    @Autowired
    public CsvReportController(CSVGeneratorService csvGeneratorService) {
        this.csvGeneratorService = csvGeneratorService;
    }

    @GetMapping(value = "/reports/get-catalog-csv", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody byte[] getCatalogCsv() {
        String catalogCsv = csvGeneratorService.generateCatalogCSVReport();
        return catalogCsv.getBytes(StandardCharsets.UTF_8);
    }

    @GetMapping(value = "/reports/get-inventory-csv", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody byte[] getInventoryCsv() {
        String inventoryCsv = csvGeneratorService.generateInventoryCSVReport();
        return inventoryCsv.getBytes(StandardCharsets.UTF_8);
    }
}
