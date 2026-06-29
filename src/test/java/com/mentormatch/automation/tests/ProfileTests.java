package com.mentormatch.automation.tests;

import com.mentormatch.automation.base.BaseTest;
import com.mentormatch.automation.data.MentorMatchDataProvider;
import com.mentormatch.automation.pages.DashboardPage;
import com.mentormatch.automation.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class ProfileTests extends BaseTest {

    @Test(groups = "sanity", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc12DeleteStudentProfile(Map<String, String> data) {
        registerStudent(true);
        DashboardPage dashboardPage = new DashboardPage(driver());
        dashboardPage.openStudentProfile();
        dashboardPage.deleteStudentProfile();
        Assert.assertTrue(new LoginPage(driver()).isAtLoginPage(), "Deleting a student profile should log the user out.");
    }

    @Test(groups = "sanity", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc25UpdateMentorProfile(Map<String, String> data) {
        loginAsMentor();
        DashboardPage dashboardPage = new DashboardPage(driver());
        dashboardPage.updateMentorProfile("Automation Technology", "75", "Automation mentor profile updated by Selenium.", "Selenium");
        Assert.assertTrue(dashboardPage.mentorProfileSaved(), "Mentor profile update should show save success.");
    }
}
