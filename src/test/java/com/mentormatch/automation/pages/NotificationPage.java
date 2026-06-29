package com.mentormatch.automation.pages;

import com.mentormatch.automation.base.BasePage;
import com.mentormatch.automation.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class NotificationPage extends BasePage {

    private final By notificationsPage = By.id("notifications-div-2");
    private final By notificationCards = By.cssSelector(".notif-card, .notif-item");
    private final By emptyState = By.cssSelector(".state-msg, .empty-state");
    private final By unreadCards = By.cssSelector(".notif-card.unread, .notif-item.unread");
    private final By markAllReadButton = By.xpath("//button[contains(normalize-space(),'Mark all')]");

    public NotificationPage(WebDriver driver) {
        super(driver);
    }

    public NotificationPage openStudentNotifications() {
        driver.get(ConfigReader.baseUrl() + "/student/notifications");
        waitForEither(notificationCards, emptyState);
        return this;
    }

    public boolean isLoaded() {
        return isDisplayed(notificationsPage) || isPresent(notificationCards) || isPresent(emptyState);
    }

    public int unreadCount() {
        return findAll(unreadCards).size();
    }

    public void markAllAsReadIfAvailable() {
        if (isPresent(markAllReadButton)) {
            click(markAllReadButton);
        }
    }

    public boolean noUnreadNotifications() {
        return unreadCount() == 0;
    }
}
