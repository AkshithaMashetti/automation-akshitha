package com.mentormatch.automation.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class ExcelUtils {

    private static final DataFormatter FORMATTER = new DataFormatter();

    private ExcelUtils() {
    }

    public static List<Map<String, String>> readSheetAsMaps(String pathValue, String sheetName) {
        Path path = ConfigReader.resolvePath(pathValue);
        if (!Files.exists(path)) {
            return List.of();
        }

        try (InputStream input = Files.newInputStream(path);
             Workbook workbook = WorkbookFactory.create(input)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                return List.of();
            }

            Row headerRow = firstNonEmptyRow(sheet);
            if (headerRow == null) {
                return List.of();
            }

            List<String> headers = readRow(headerRow);
            List<Map<String, String>> records = new ArrayList<>();
            for (int i = headerRow.getRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                List<String> values = readRow(row);
                if (values.stream().allMatch(String::isBlank)) {
                    continue;
                }
                Map<String, String> record = new LinkedHashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    String header = headers.get(j);
                    if (!header.isBlank()) {
                        record.put(header, j < values.size() ? values.get(j) : "");
                    }
                }
                records.add(record);
            }
            return records;
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read Excel test data: " + path, e);
        }
    }

    public static Map<String, String> findTestCase(String testCaseId) {
        String path = ConfigReader.get("testDataPath", "src/test/resources/testdata/MentorMatch_Testing.xlsx");
        String sheet = ConfigReader.get("testDataSheet", "TEST CASES");
        return readSheetAsMaps(path, sheet)
                .stream()
                .filter(row -> row.values().stream().anyMatch(value -> matchesTestCase(value, testCaseId)))
                .findFirst()
                .orElse(Map.of());
    }

    public static Map<String, String> testDataForCase(String testCaseId) {
        Map<String, String> row = findTestCase(testCaseId);
        if (row.isEmpty()) {
            return Map.of();
        }
        String block = findValue(row, "Test Data");
        return parseKeyValueBlock(block);
    }

    public static Map<String, String> parseKeyValueBlock(String block) {
        if (block == null || block.isBlank()) {
            return Map.of();
        }
        Map<String, String> data = new LinkedHashMap<>();
        for (String line : block.split("\\R")) {
            int separator = line.indexOf(':');
            if (separator <= 0) {
                continue;
            }
            String key = normalizeKey(line.substring(0, separator));
            String value = line.substring(separator + 1).trim();
            if (!key.isBlank()) {
                data.put(key, value);
            }
        }
        return data;
    }

    public static String normalizeKey(String rawKey) {
        String compact = rawKey == null ? "" : rawKey.trim().toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", " ");
        String[] parts = compact.trim().split("\\s+");
        if (parts.length == 0 || parts[0].isBlank()) {
            return "";
        }
        StringBuilder normalized = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            normalized.append(parts[i].substring(0, 1).toUpperCase(Locale.ROOT)).append(parts[i].substring(1));
        }
        return normalized.toString();
    }

    private static Row firstNonEmptyRow(Sheet sheet) {
        for (Row row : sheet) {
            if (readRow(row).stream().anyMatch(value -> !value.isBlank())) {
                return row;
            }
        }
        return null;
    }

    private static List<String> readRow(Row row) {
        List<String> values = new ArrayList<>();
        short lastCell = row.getLastCellNum();
        for (int i = 0; i < Math.max(0, lastCell); i++) {
            Cell cell = row.getCell(i);
            values.add(cell == null ? "" : FORMATTER.formatCellValue(cell).trim());
        }
        return values;
    }

    private static String findValue(Map<String, String> row, String preferredHeader) {
        return row.entrySet()
                .stream()
                .filter(entry -> entry.getKey().equalsIgnoreCase(preferredHeader))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("");
    }

    private static boolean matchesTestCase(String value, String testCaseId) {
        if (value == null || value.isBlank()) {
            return false;
        }
        String expected = testCaseId.toUpperCase(Locale.ROOT);
        String normalizedValue = value.toUpperCase(Locale.ROOT).replaceAll("[^A-Z0-9]", "");
        String normalizedExpected = expected.replaceAll("[^A-Z0-9]", "");
        String withoutLeadingZero = normalizedExpected.replaceFirst("TC0", "TC");
        return normalizedValue.contains(normalizedExpected) || normalizedValue.contains(withoutLeadingZero);
    }
}
