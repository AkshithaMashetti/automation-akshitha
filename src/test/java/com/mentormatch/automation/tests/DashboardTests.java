package com.mentormatch.automation.tests;

import com.mentormatch.automation.base.BaseTest;
import com.mentormatch.automation.data.MentorMatchDataProvider;
import com.mentormatch.automation.pages.DashboardPage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Map;

public class DashboardTests extends BaseTest {

    @Test(groups = "smoke", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc10StudentDashboardDisplay(Map<String, String> data) {
        loginAsStudent();
        Assert.assertTrue(new WebDriverWait(driver(), Duration.ofSeconds(30))
                .until(d -> d.getCurrentUrl().contains("student")));
    }

    @Test(groups = "smoke", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc11StudentLoginRedirection(Map<String, String> data) {
        loginAsStudent();
        Assert.assertTrue(driver().getCurrentUrl().contains("/student/dashboard") || driver().getCurrentUrl().contains("/student/profile"),
                "Student login should redirect to a student route.");
    }

    @Test(groups = "regression", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc15NavigateMentorList(Map<String, String> data) {

        loginAsStudent();

        // ✅ Debug: check where we landed after login
        String currentUrl = driver().getCurrentUrl();
        System.out.println("Current URL after login: " + currentUrl);

        // ✅ FIX: If redirected to profile, go to dashboard
        if (currentUrl.contains("/profile")) {
            driver().get("https://mentormatch-green.netlify.app/student/dashboard");
        }

        // ✅ Wait until dashboard is loaded
        new WebDriverWait(driver(), Duration.ofSeconds(30))
                .until(ExpectedConditions.urlContains("dashboard"));

        DashboardPage dashboardPage = new DashboardPage(driver());

        // ✅ Click "Book Session"
        dashboardPage.clickBookSession();

        // ✅ Wait for navigation to mentors page
        new WebDriverWait(driver(), Duration.ofSeconds(30))
                .until(ExpectedConditions.urlContains("mentors"));

        // ✅ Assertion with helpful message
        Assert.assertTrue(
                driver().getCurrentUrl().contains("/student/mentors"),
                "Dashboard book action failed. Current URL: " + driver().getCurrentUrl()
        );
    }


//    @Test(groups = "regression", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
//    public void tc23DashboardSummaryData(Map<String, String> data) {
//
//        loginAsStudent();
//
//        String currentUrl = driver().getCurrentUrl();
//        System.out.println("Current URL after login: " + currentUrl);
//
//        // ✅ FIX: If login lands on profile → go to dashboard
//        if (currentUrl.contains("/profile") || currentUrl.contains("/auth")) {
//            driver().get("https://mentormatch-green.netlify.app/student/dashboard");
//        }
//
//        // ✅ Wait for correct page (safe wait)
//        new WebDriverWait(driver(), Duration.ofSeconds(30))
//                .until(d -> d.getCurrentUrl().contains("dashboard"));
//
//        System.out.println("After redirect URL: " + driver().getCurrentUrl());
//
//        // ✅ Now validate stats
//        Assert.assertTrue(
//                new DashboardPage(driver()).dashboardStatsAreNumeric(),
//                "Dashboard stats not visible or invalid. Current URL: " + driver().getCurrentUrl()
//        );
    //}
}
