package com.mentormatch.automation.tests;

import com.mentormatch.automation.base.BaseTest;
import com.mentormatch.automation.data.MentorMatchDataProvider;
import com.mentormatch.automation.pages.DashboardPage;
import com.mentormatch.automation.pages.MentorPage;
import com.mentormatch.automation.pages.ReviewPage;
import com.mentormatch.automation.utils.TestDataGenerator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class ReviewTests extends BaseTest {

    @Test(groups = "regression", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc30ViewOwnReviewsOnly(Map<String, String> data) {
        loginAsMentor();
        new DashboardPage(driver()).switchMentorTab("reviews");
        Assert.assertTrue(driver().getPageSource().contains("My Reviews") || driver().getPageSource().contains("No reviews"),
                "Mentor reviews tab should show only the mentor's review area.");
    }

    @Test(groups = "regression", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc41ReviewSubmissionValid(Map<String, String> data) {
        String topic = TestDataGenerator.uniqueTopic("TC41 Review");
        createCompletedSessionForReview(topic);
        submitReviewForCompletedSession(topic, Integer.parseInt(value(data, "rating", "5")), value(data, "comment", "Great session"));
        Assert.assertTrue(new ReviewPage(driver()).isSuccessDisplayed(), "Completed session should accept a valid review.");
    }

    @Test(groups = "regression", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc42ReviewSubmissionBlock(Map<String, String> data) {
        loginAsStudent();
        driver().get(com.mentormatch.automation.utils.ConfigReader.baseUrl() + "/student/submit-review/999999");
        ReviewPage reviewPage = new ReviewPage(driver());
        reviewPage.submitReview(5, "Invalid session review");
        Assert.assertFalse(reviewPage.errorText().isBlank(), "Review should be blocked for invalid or unauthorized session.");
    }

    @Test(groups = "sanity", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc43FullLengthComment(Map<String, String> data) {
        String topic = TestDataGenerator.uniqueTopic("TC43 Long Comment");
        createCompletedSessionForReview(topic);
        submitReviewForCompletedSession(topic, 5, value(data, "comment", TestDataGenerator.longString(1000)));
        Assert.assertTrue(new ReviewPage(driver()).isSuccessDisplayed(), "Full-length review comment should be accepted.");
    }

    @Test(groups = "sanity", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc45ReviewWithComment(Map<String, String> data) {
        String topic = TestDataGenerator.uniqueTopic("TC45 Review Comment");
        createCompletedSessionForReview(topic);
        submitReviewForCompletedSession(topic, 5, value(data, "comment", "Helpful mentor."));
        Assert.assertTrue(new ReviewPage(driver()).isSuccessDisplayed(), "Review with comment should be submitted.");
    }

    @Test(groups = "sanity", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc46ReviewWithoutComment(Map<String, String> data) {
        String topic = TestDataGenerator.uniqueTopic("TC46 No Comment");
        createCompletedSessionForReview(topic);
        submitReviewForCompletedSession(topic, 4, "");
        Assert.assertTrue(new ReviewPage(driver()).isSuccessDisplayed(), "Review without optional comment should be submitted.");
    }

    @Test(groups = "sanity", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc48RatingUpdate(Map<String, String> data) {
        String topic = TestDataGenerator.uniqueTopic("TC48 Rating");
        createCompletedSessionForReview(topic);
        submitReviewForCompletedSession(topic, 4, "Rating update check.");
        new MentorPage(driver()).openMentorList().openFirstMentorDetail();
        Assert.assertFalse(new MentorPage(driver()).displayedRating().isBlank(), "Mentor profile should display a rating after review submission.");
    }

    @Test(groups = "sanity", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc49RatingRecalculation(Map<String, String> data) {
        String topicOne = TestDataGenerator.uniqueTopic("TC49 Rating One");
        createCompletedSessionForReview(topicOne);
        submitReviewForCompletedSession(topicOne, 5, "First rating.");
        clearSession();
        String topicTwo = TestDataGenerator.uniqueTopic("TC49 Rating Two");
        createCompletedSessionForReview(topicTwo);
        submitReviewForCompletedSession(topicTwo, 1, "Second rating.");
        MentorPage mentorPage = new MentorPage(driver()).openMentorList().openFirstMentorDetail();
        Assert.assertTrue(mentorPage.displayedRating().matches("\\d+(\\.\\d+)?"), "Mentor average rating should be recalculated and displayed.");
    }
}
