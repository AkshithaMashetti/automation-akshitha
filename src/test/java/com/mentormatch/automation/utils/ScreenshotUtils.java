package com.mentormatch.automation.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtils {

    private ScreenshotUtils() {
    }

    public static String capture(WebDriver driver, String testName) {
        if (driver == null) {
            return "No WebDriver instance was available for screenshot capture.";
        }

        String directory = ConfigReader.get("screenshotDir", "screenshots");
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
        String safeName = testName.replaceAll("[^a-zA-Z0-9._-]", "_");
        Path target = ConfigReader.resolvePath(directory).resolve(safeName + "_" + timestamp + ".png");

        try {
            Files.createDirectories(target.getParent());
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(source.toPath(), target);
            return target.toAbsolutePath().toString();
        } catch (IOException | RuntimeException e) {
            return "Screenshot capture failed: " + e.getMessage();
        }
    }
}
