package com.mentormatch.automation.pages;

import com.mentormatch.automation.base.BasePage;
import com.mentormatch.automation.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Locale;

public class SessionPage extends BasePage {

    private final By studentSessionsPage = By.id("my-sessions-div-2");
    private final By studentSessionCards = By.cssSelector("#my-sessions-div-18 .session-card");
    private final By emptyStudentSessions = By.id("my-sessions-div-13");
    private final By mentorDashboard = By.id("mentor-dashboard-div-44");
    private final By mentorSessionsTab = By.id("mentor-dashboard-button-13");
    private final By mentorSessionCards = By.cssSelector("#mentor-dashboard-div-105 .session-card");
    private final By emptyMentorSessions = By.id("mentor-dashboard-div-101");
    private final By acceptMeetingLinkInput = By.id("accept-meeting-link-input");
    private final By acceptConfirmButton = By.id("accept-confirm-btn");
    private final By cancelReasonInput = By.id("cancelReason");
    private final By confirmCancellationButton = By.id("confirm-cancellation-btn");

    public SessionPage(WebDriver driver) {
        super(driver);
    }

    public SessionPage openStudentSessions() {
        driver.get(ConfigReader.baseUrl() + "/student/sessions");
        waitForEither(studentSessionCards, emptyStudentSessions);
        return this;
    }

    public boolean isStudentSessionsLoaded() {
        return isDisplayed(studentSessionsPage);
    }

    public int studentSessionCount() {
        return findAll(studentSessionCards).size();
    }

    public boolean hasStudentSessionTopic(String topic) {
        return isPresent(studentCardByTopic(topic));
    }

    public boolean hasStudentStatusForTopic(String topic, String status) {
        By card = studentCardByTopic(topic);
        return isPresent(card) && driver.findElement(card).getText().toUpperCase(Locale.ROOT).contains(status);
    }

    public void openReviewForTopic(String topic) {
        WebElement card = wait.visible(studentCardByTopic(topic));
        WebElement reviewButton = card.findElement(By.cssSelector("[id^='leave-review-btn-']"));
        scrollIntoView(reviewButton);
        reviewButton.click();
        wait.urlContains("/student/submit-review/");
    }

    public SessionPage openMentorSessions() {
        driver.get(ConfigReader.baseUrl() + "/mentor/dashboard");
        wait.visible(mentorDashboard);
        click(mentorSessionsTab);
        waitForEither(mentorSessionCards, emptyMentorSessions);
        return this;
    }

    public boolean isMentorSessionsLoaded() {
        return isDisplayed(mentorDashboard);
    }

    public List<String> mentorSessionTexts() {
        return findAll(mentorSessionCards).stream().map(WebElement::getText).toList();
    }

    public boolean hasMentorSessionTopic(String topic) {
        return isPresent(mentorCardByTopic(topic));
    }

    public void acceptSessionByTopic(String topic, String meetingLink) {
        WebElement card = wait.visible(mentorCardByTopic(topic));
        WebElement acceptButton = card.findElement(By.cssSelector("[id^='accept-session-btn-']"));
        scrollIntoView(acceptButton);
        acceptButton.click();
        type(acceptMeetingLinkInput, meetingLink);
        click(acceptConfirmButton);
        waitForStatus(topic, "ACCEPTED");
    }

    public boolean attemptAcceptWithInvalidLink(String topic, String invalidLink) {
        WebElement card = wait.visible(mentorCardByTopic(topic));
        WebElement acceptButton = card.findElement(By.cssSelector("[id^='accept-session-btn-']"));
        scrollIntoView(acceptButton);
        acceptButton.click();
        type(acceptMeetingLinkInput, invalidLink);
        click(acceptConfirmButton);
        wait.documentReady();
        return cardText(topic).contains("ACCEPTED");
    }

    public void rejectSessionByTopic(String topic, String reason) {
        WebElement card = wait.visible(mentorCardByTopic(topic));
        WebElement rejectButton = card.findElement(By.cssSelector("[id^='reject-session-btn-']"));
        scrollIntoView(rejectButton);
        rejectButton.click();
        type(cancelReasonInput, reason);
        click(confirmCancellationButton);
        waitForStatus(topic, "REJECTED");
    }

    public void cancelAcceptedSessionByTopic(String topic, String reason) {
        WebElement card = wait.visible(mentorCardByTopic(topic));
        WebElement cancelButton = card.findElement(By.cssSelector("[id^='cancel-session-btn-']"));
        scrollIntoView(cancelButton);
        cancelButton.click();
        type(cancelReasonInput, reason);
        click(confirmCancellationButton);
        waitForStatus(topic, "CANCELLED");
    }

    public void completeSessionByTopic(String topic) {
        WebElement card = wait.visible(mentorCardByTopic(topic));
        WebElement completeButton = card.findElement(By.cssSelector("[id^='complete-session-btn-']"));
        scrollIntoView(completeButton);
        completeButton.click();
        driver.switchTo().alert().accept();
        waitForStatus(topic, "COMPLETED");
    }

    public boolean completeButtonHiddenForPending(String topic) {
        WebElement card = wait.visible(mentorCardByTopic(topic));
        return card.findElements(By.cssSelector("[id^='complete-session-btn-']")).isEmpty()
                && card.getText().contains("PENDING");
    }

    public String cardText(String topic) {
        return wait.visible(mentorCardByTopic(topic)).getText();
    }

    private void waitForStatus(String topic, String status) {
        By card = mentorCardByTopic(topic);
        wait.textContains(card, status);
    }

    private By studentCardByTopic(String topic) {
        return By.xpath("//div[contains(@class,'session-card')][.//*[contains(normalize-space(), " + xpathLiteral(topic) + ")]]");
    }

    private By mentorCardByTopic(String topic) {
        return By.xpath("//div[contains(@class,'session-card')][.//*[contains(normalize-space(), " + xpathLiteral(topic) + ")]]");
    }

    private String xpathLiteral(String value) {
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        return "\"" + value + "\"";
    }
}
