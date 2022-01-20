package com.adiSuper.shopifyIMS.controller;

import com.adiSuper.shopifyIMS.service.CSVGeneratorService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;


@RestController
public class CsvReportController {

    private final CSVGeneratorService csvGeneratorService;

    @Autowired
    public CsvReportController(CSVGeneratorService csvGeneratorService) {
        this.csvGeneratorService = csvGeneratorService;
    }

    @GetMapping(value = "/reports/get-catalog-csv", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody byte[] getCatalogCsv() throws IOException {
        Path catalogCsvPath = csvGeneratorService.generateCatalogCSVReport();
        return IOUtils.toByteArray(Files.newInputStream(catalogCsvPath));
    }

    @GetMapping(value = "/reports/get-inventory-csv", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody byte[] getInventoryCsv() throws IOException {
        Path inventoryCsvPath = csvGeneratorService.generateInventoryCSVReport();
        return IOUtils.toByteArray(Files.newInputStream(inventoryCsvPath));
    }
}
