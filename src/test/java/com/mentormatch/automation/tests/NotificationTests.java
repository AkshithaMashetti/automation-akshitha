package com.mentormatch.automation.tests;

import com.mentormatch.automation.base.BaseTest;
import com.mentormatch.automation.data.MentorMatchDataProvider;
import com.mentormatch.automation.pages.NotificationPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class NotificationTests extends BaseTest {

    @Test(groups = "regression", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc20ViewNotifications(Map<String, String> data) {
        loginAsStudent();
        Assert.assertTrue(new NotificationPage(driver()).openStudentNotifications().isLoaded(),
                "Student notifications page should load.");
    }

    @Test(groups = "regression", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc21ReadUnreadNotifications(Map<String, String> data) {
        loginAsStudent();
        NotificationPage notificationPage = new NotificationPage(driver()).openStudentNotifications();
        notificationPage.markAllAsReadIfAvailable();
        Assert.assertTrue(notificationPage.noUnreadNotifications(), "Mark all read should clear unread notification state.");
    }
}
