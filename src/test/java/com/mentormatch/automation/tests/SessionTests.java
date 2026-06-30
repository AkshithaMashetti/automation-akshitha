package com.mentormatch.automation.tests;

import com.mentormatch.automation.base.BaseTest;
import com.mentormatch.automation.data.MentorMatchDataProvider;
import com.mentormatch.automation.pages.DashboardPage;
import com.mentormatch.automation.pages.LoginPage;
import com.mentormatch.automation.pages.MentorPage;
import com.mentormatch.automation.pages.SessionPage;
import com.mentormatch.automation.utils.ConfigReader;
import com.mentormatch.automation.utils.TestDataGenerator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class SessionTests extends BaseTest {

    @Test(groups = "regression", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc18ViewOwnSessionList(Map<String, String> data) {
        loginAsStudent();
        Assert.assertTrue(new SessionPage(driver()).openStudentSessions().isStudentSessionsLoaded(),
                "Student should be able to view their own sessions page.");
    }

    @Test(groups = "sanity", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc19RestrictOtherSessions(Map<String, String> data) {
        loginAsStudent();
        driver().get(ConfigReader.baseUrl() + "/student/sessions/999999");
        SessionPage sessionPage = new SessionPage(driver());
        Assert.assertTrue(sessionPage.isStudentSessionsLoaded(), "Unauthorized session detail route should resolve to own sessions list.");
    }

//    @Test(groups = "regression", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
//    public void tc27ViewAssignedSessionsOnly(Map<String, String> data) {
//        loginAsMentor();
//        SessionPage sessionPage = new SessionPage(driver()).openMentorSessions();
//        Assert.assertTrue(sessionPage.isMentorSessionsLoaded(), "Mentor assigned sessions view should load.");
//    }

//    @Test(groups = "sanity", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
//    public void tc28AcceptSessionWithInvalidLink(Map<String, String> data) {
//        String topic = TestDataGenerator.uniqueTopic("TC28 Invalid Link");
//        createPendingSessionForNewStudent(topic);
//        loginAsMentor();
//        boolean accepted = new SessionPage(driver()).openMentorSessions()
//                .attemptAcceptWithInvalidLink(topic, value(data, "meetingLink", "not-a-valid-url"));
//        Assert.assertFalse(accepted, "Invalid meeting link should not be accepted.");
//    }
//
//    @Test(groups = "regression", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
//    public void tc29RejectCancelSessionWithReason(Map<String, String> data) {
//        String topic = TestDataGenerator.uniqueTopic("TC29 Cancel");
//        createPendingSessionForNewStudent(topic);
//        loginAsMentor();
//        SessionPage sessionPage = new SessionPage(driver()).openMentorSessions();
//        sessionPage.acceptSessionByTopic(topic, TestDataGenerator.meetingLink());
//        sessionPage.cancelAcceptedSessionByTopic(topic, value(data, "reason", "Schedule conflict"));
//        Assert.assertTrue(sessionPage.cardText(topic).contains("CANCELLED"), "Accepted session should be cancellable with a reason.");
//    }
//
//    @Test(groups = "smoke", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
//    public void tc31SessionBookingValid(Map<String, String> data) {
//        ensureMentorAvailability(true);
//        loginAsStudent();
//        String topic = TestDataGenerator.uniqueTopic("TC31 Booking");
//        MentorPage mentorPage = new MentorPage(driver()).openMentorList().openFirstMentorDetail();
//        mentorPage.bookSession(topic, TestDataGenerator.futureDate(2), TestDataGenerator.stableFutureTime(), "SINGLE", "1", value(data, "message", "Smoke booking"));
//        Assert.assertTrue(mentorPage.isBookingSuccessDisplayed(), "Valid session booking should show a success banner.");
//    }
//
//    @Test(groups = "smoke", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
//    public void tc32SessionPendingStatus(Map<String, String> data) {
//        String topic = TestDataGenerator.uniqueTopic("TC32 Pending");
//        UserAccount student = createPendingSessionForNewStudent(topic);
//        new LoginPage(driver()).open().login(student.email(), student.password());
//        new DashboardPage(driver()).waitForStudentLanding();
//        SessionPage sessionPage = new SessionPage(driver()).openStudentSessions();
//        Assert.assertTrue(sessionPage.hasStudentStatusForTopic(topic, "PENDING"), "Booked session should be visible with PENDING status.");
//    }
//
//    @Test(groups = "regression", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
//    public void tc33MandatoryFieldValidation(Map<String, String> data) {
//        loginAsStudent();
//        boolean disabled = new MentorPage(driver()).openMentorList().openFirstMentorDetail().isBookingSubmitDisabled();
//        Assert.assertTrue(disabled, "Booking submit should stay disabled until mandatory fields are completed.");
//    }
//
//    @Test(groups = "regression", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
//    public void tc34InvalidDate(Map<String, String> data) {
//        loginAsStudent();
//        boolean blocked = new MentorPage(driver()).openMentorList().openFirstMentorDetail()
//                .invalidPastDateIsBlocked(TestDataGenerator.pastDate(1), TestDataGenerator.stableFutureTime());
//        Assert.assertTrue(blocked, "Past booking date/time should be blocked.");
//    }
//
//    @Test(groups = "smoke", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
//    public void tc35AcceptSession(Map<String, String> data) {
//        String topic = TestDataGenerator.uniqueTopic("TC35 Accept");
//        createPendingSessionForNewStudent(topic);
//        loginAsMentor();
//        SessionPage sessionPage = new SessionPage(driver()).openMentorSessions();
//        sessionPage.acceptSessionByTopic(topic, TestDataGenerator.meetingLink());
//        Assert.assertTrue(sessionPage.cardText(topic).contains("ACCEPTED"), "Mentor should be able to accept a pending session.");
//    }
//
//    @Test(groups = "smoke", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
//    public void tc36CompleteSession(Map<String, String> data) {
//        String topic = TestDataGenerator.uniqueTopic("TC36 Complete");
//        createPendingSessionForNewStudent(topic);
//        loginAsMentor();
//        SessionPage sessionPage = new SessionPage(driver()).openMentorSessions();
//        sessionPage.acceptSessionByTopic(topic, TestDataGenerator.meetingLink());
//        sessionPage.completeSessionByTopic(topic);
//        Assert.assertTrue(sessionPage.cardText(topic).contains("COMPLETED"), "Accepted session should be completed by mentor.");
//    }
//
//    @Test(groups = "regression", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
//    public void tc37RejectSession(Map<String, String> data) {
//        String topic = TestDataGenerator.uniqueTopic("TC37 Reject");
//        createPendingSessionForNewStudent(topic);
//        loginAsMentor();
//        SessionPage sessionPage = new SessionPage(driver()).openMentorSessions();
//        sessionPage.rejectSessionByTopic(topic, value(data, "reason", "Unavailable"));
//        Assert.assertTrue(sessionPage.cardText(topic).contains("REJECTED"), "Mentor should reject a pending session.");
//    }
//
//    @Test(groups = "regression", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
//    public void tc38InvalidTransition(Map<String, String> data) {
//        String topic = TestDataGenerator.uniqueTopic("TC38 Transition");
//        createPendingSessionForNewStudent(topic);
//        loginAsMentor();
//        Assert.assertTrue(new SessionPage(driver()).openMentorSessions().completeButtonHiddenForPending(topic),
//                "Pending sessions should not expose a direct Complete transition.");
//    }
//
//    @Test(groups = "sanity", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
//    public void tc39StudentSessionVisibility(Map<String, String> data) {
//        String topic = TestDataGenerator.uniqueTopic("TC39 Private Session");
//        createPendingSessionForNewStudent(topic);
//        registerStudent(true);
//        SessionPage sessions = new SessionPage(driver()).openStudentSessions();
//        Assert.assertFalse(sessions.hasStudentSessionTopic(topic), "A second student must not see another student's session.");
//    }
}
