package com.mentormatch.automation.listeners;

import com.aventstack.extentreports.*;
import com.mentormatch.automation.base.BaseTest;
import com.mentormatch.automation.utils.ExtentReport;
import com.mentormatch.automation.utils.ScreenshotUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.*;

public class TestListener implements ITestListener {

    private static final Logger LOGGER =
            LogManager.getLogger(TestListener.class);

    private static final ExtentReports extent =
            ExtentReport.getInstance();

    private static final ThreadLocal<ExtentTest> test =
            new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {

        ExtentTest extentTest =
                extent.createTest(result.getMethod().getMethodName());

        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        test.get().pass("Test Passed");

        LOGGER.info("Test passed: {}",
                result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {

        String screenshot =
                ScreenshotUtils.capture(
                        BaseTest.currentDriver(),
                        result.getMethod().getMethodName());

        test.get().fail(result.getThrowable());

        try {
            test.get().addScreenCaptureFromPath(screenshot);
        } catch (Exception e) {
            test.get().warning("Unable to attach screenshot");
        }

        Reporter.log("Screenshot: " + screenshot, true);

        LOGGER.error("Test failed: {}. Screenshot: {}",
                result.getMethod().getMethodName(),
                screenshot);
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        test.get().skip("Test Skipped");

        LOGGER.warn("Test skipped: {}",
                result.getMethod().getMethodName());
    }

    @Override
    public void onFinish(ITestContext context) {

        extent.flush();
    }
}