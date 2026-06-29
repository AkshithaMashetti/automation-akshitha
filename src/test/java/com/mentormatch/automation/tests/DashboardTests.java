package com.mentormatch.automation.tests;

import com.mentormatch.automation.base.BaseTest;
import com.mentormatch.automation.data.MentorMatchDataProvider;
import com.mentormatch.automation.pages.DashboardPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class DashboardTests extends BaseTest {

    @Test(groups = "smoke", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc10StudentDashboardDisplay(Map<String, String> data) {
        loginAsStudent();
        Assert.assertTrue(new DashboardPage(driver()).isStudentDashboardDisplayed(), "Student dashboard should be displayed.");
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
        DashboardPage dashboardPage = new DashboardPage(driver());
        dashboardPage.clickBookSession();
        Assert.assertTrue(driver().getCurrentUrl().contains("/student/mentors"), "Dashboard book action should navigate to mentor list.");
    }

    @Test(groups = "regression", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc23DashboardSummaryData(Map<String, String> data) {
        loginAsStudent();
        Assert.assertTrue(new DashboardPage(driver()).dashboardStatsAreNumeric(), "Dashboard summary stats should be numeric.");
    }
}
