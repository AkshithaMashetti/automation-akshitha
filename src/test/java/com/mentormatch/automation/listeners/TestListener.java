package com.mentormatch.automation.listeners;

import com.mentormatch.automation.base.BaseTest;
import com.mentormatch.automation.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

public class TestListener implements ITestListener {

    private static final Logger LOGGER = LogManager.getLogger(TestListener.class);

    @Override
    public void onTestFailure(ITestResult result) {
        String screenshot = ScreenshotUtils.capture(BaseTest.currentDriver(), result.getMethod().getMethodName());
        Reporter.log("Screenshot: " + screenshot, true);
        LOGGER.error("Test failed: {}. Screenshot: {}", result.getMethod().getMethodName(), screenshot);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOGGER.warn("Test skipped: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOGGER.info("Test passed: {}", result.getMethod().getMethodName());
    }
}
