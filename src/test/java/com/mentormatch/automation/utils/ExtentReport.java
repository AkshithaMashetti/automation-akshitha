package com.mentormatch.automation.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReport{

    private static ExtentReports extent;

    public static ExtentReports getInstance() {

        if (extent == null) {

            ExtentSparkReporter spark =
                    new ExtentSparkReporter("test-output/ExtentReport.html");

            spark.config().setDocumentTitle("MentorMatch Report");
            spark.config().setReportName("Automation Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("Project", "MentorMatch");
            extent.setSystemInfo("Framework", "Selenium + TestNG");
        }

        return extent;
    }
}