package com.mentormatch.automation.tests;

import com.mentormatch.automation.base.BaseTest;
import com.mentormatch.automation.data.MentorMatchDataProvider;
import com.mentormatch.automation.pages.DashboardPage;
import com.mentormatch.automation.pages.LoginPage;
import com.mentormatch.automation.pages.MentorPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class MentorTests extends BaseTest {

//    @Test(groups = "smoke", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
//    public void tc14BrowseActiveMentors(Map<String, String> data) {
//        loginAsStudent();
//        MentorPage mentorPage = new MentorPage(driver()).openMentorList().setAvailableOnly(true);
//        Assert.assertTrue(mentorPage.isMentorListLoaded(), "Browse mentors page should load active mentor results or empty state.");
//    }

//    @Test(groups = "smoke", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
//    public void tc16OpenMentorDetailPage(Map<String, String> data) {
//        loginAsStudent();
//        MentorPage mentorPage = new MentorPage(driver()).openMentorList().openFirstMentorDetail();
//        Assert.assertTrue(mentorPage.isDetailLoaded(), "Mentor detail page should be displayed.");
//    }

//    @Test(groups = "regression", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
//    public void tc17ViewMentorDetails(Map<String, String> data) {
//        loginAsStudent();
//        Assert.assertTrue(new MentorPage(driver()).openMentorList().openFirstMentorDetail().isDetailLoaded(),
//                "Mentor details should open from mentor list.");
//    }

//    @Test(groups = "regression", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
//    public void tc24ViewMentorProfile(Map<String, String> data) {
//        loginAsStudent();
//        MentorPage mentorPage = new MentorPage(driver()).openMentorList().openFirstMentorDetail();
//        Assert.assertFalse(mentorPage.mentorName().isBlank(), "Mentor profile should display mentor name.");
//    }

//    @Test(groups = "sanity", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
//    public void tc26UpdateAvailabilityInSearch(Map<String, String> data) {
//        ensureMentorAvailability(true);
//        loginAsStudent();
//        MentorPage mentorPage = new MentorPage(driver()).openMentorList().setAvailableOnly(true);
//        Assert.assertTrue(mentorPage.mentorCount() > 0, "Available mentor should appear in student search when availability is on.");
//    }
    @Test(groups = "regression", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc23DashboardSummaryData(Map<String, String> data) {
//        new LoginPage(driver()).open().login(data.get("email"), data.get("password"));
        loginAsMentor();
        new DashboardPage(driver()).waitForStudentLanding();
        Assert.assertTrue(new DashboardPage(driver()).dashboardStatsAreNumeric(), "Dashboard summary stats should be numeric.");
    }
    @Test(groups = "regression", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc24ViewMentorProfile(Map<String, String> data) {
        loginAsMentor();
        MentorPage mentorPage = new MentorPage(driver()).openProfile();
        Assert.assertFalse(mentorPage.mentorName1().isBlank(), "Mentor profile should display mentor name.");
    }
    //DEFECT
    @Test(groups = "sanity", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc25UpdateMentorProfile(Map<String, String> data) {
        loginAsMentor();
        DashboardPage dashboardPage = new DashboardPage(driver());
        MentorPage mentorPage = new MentorPage(driver()).openProfile();
        mentorPage.updateMentorProfile("Automation Technology", "-1", "Automation mentor profile updated by Selenium.", "Selenium");
        Assert.assertTrue(mentorPage.mentorProfileSaved(), "Mentor profile update should show save success.");
    }
    @Test(groups = "regression", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc27ViewAssignedSessionsOnly(Map<String, String> data) {
        loginAsMentor();

        //SessionPage sessionPage = new SessionPage(driver()).openMentorSessions();
        MentorPage mentorPage = new MentorPage(driver()).openSessionPage();
        Assert.assertTrue(mentorPage.isMentorSessionsLoaded(), "Mentor assigned sessions view should load.");
    }
}
