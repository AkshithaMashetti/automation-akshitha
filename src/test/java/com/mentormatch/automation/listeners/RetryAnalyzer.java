package com.mentormatch.automation.listeners;

import com.mentormatch.automation.utils.ConfigReader;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int attempt = 0;

    @Override
    public boolean retry(ITestResult result) {
        int maxRetries = ConfigReader.getInt("retryCount", 1);
        if (attempt < maxRetries) {
            attempt++;
            return true;
        }
        return false;
    }
}
