package com.mentormatch.automation.tests;

import com.mentormatch.automation.base.BaseTest;
import com.mentormatch.automation.data.MentorMatchDataProvider;
import com.mentormatch.automation.pages.NotificationPage;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
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

        // ✅ Step 1: Login
        loginAsStudent();

        String url = driver().getCurrentUrl();
        System.out.println("Current URL after login: " + url);

        // ✅ Step 2: Fix redirect issue
        if (url.contains("/profile") || url.contains("/auth")) {
            driver().get("https://mentormatch-green.netlify.app/student/dashboard");
        }

        // ✅ Step 3: Open notifications
        NotificationPage notificationPage = new NotificationPage(driver())
                .openStudentNotifications();

        // ✅ Step 4: Mark all as read
        notificationPage.markAllAsReadIfAvailable();

        // ✅ Step 5: WAIT for unread to disappear (THIS WAS MISSING)
        WebDriverWait wait = new WebDriverWait(driver(), Duration.ofSeconds(30));

        boolean cleared = wait.until(d -> notificationPage.noUnreadNotifications());

        System.out.println("No unread notifications: " + cleared);

        // ✅ Step 6: Assert
        Assert.assertTrue(
                cleared,
                "Unread notifications were NOT cleared after marking all as read."
        );
    }
}
