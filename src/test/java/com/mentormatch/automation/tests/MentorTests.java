package com.mentormatch.automation.tests;

import com.mentormatch.automation.base.BaseTest;
import com.mentormatch.automation.data.MentorMatchDataProvider;
import com.mentormatch.automation.pages.MentorPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class MentorTests extends BaseTest {

    @Test(groups = "smoke", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc14BrowseActiveMentors(Map<String, String> data) {
        loginAsStudent();
        MentorPage mentorPage = new MentorPage(driver()).openMentorList().setAvailableOnly(true);
        Assert.assertTrue(mentorPage.isMentorListLoaded(), "Browse mentors page should load active mentor results or empty state.");
    }

    @Test(groups = "smoke", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc16OpenMentorDetailPage(Map<String, String> data) {
        loginAsStudent();
        MentorPage mentorPage = new MentorPage(driver()).openMentorList().openFirstMentorDetail();
        Assert.assertTrue(mentorPage.isDetailLoaded(), "Mentor detail page should be displayed.");
    }

    @Test(groups = "regression", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc17ViewMentorDetails(Map<String, String> data) {
        loginAsStudent();
        Assert.assertTrue(new MentorPage(driver()).openMentorList().openFirstMentorDetail().isDetailLoaded(),
                "Mentor details should open from mentor list.");
    }

    @Test(groups = "regression", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc24ViewMentorProfile(Map<String, String> data) {
        loginAsStudent();
        MentorPage mentorPage = new MentorPage(driver()).openMentorList().openFirstMentorDetail();
        Assert.assertFalse(mentorPage.mentorName().isBlank(), "Mentor profile should display mentor name.");
    }

    @Test(groups = "sanity", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc26UpdateAvailabilityInSearch(Map<String, String> data) {
        ensureMentorAvailability(true);
        loginAsStudent();
        MentorPage mentorPage = new MentorPage(driver()).openMentorList().setAvailableOnly(true);
        Assert.assertTrue(mentorPage.mentorCount() > 0, "Available mentor should appear in student search when availability is on.");
    }
}
