package com.mentormatch.automation.pages;

import com.mentormatch.automation.base.BasePage;
import com.mentormatch.automation.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class MentorPage extends BasePage {

    private final By mentorListPage = By.id("browse-mentors-div-2");
    private final By mentorCards = By.cssSelector(".mentor-card");
    private final By emptyMentorState = By.id("browse-mentors-div-44");
    private final By searchSkillInput = By.id("filter-skill-input");
    private final By availableOnlyCheckbox = By.id("filter-available-checkbox");
    private final By detailPage = By.id("mentor-detail-div-2");
    private final By detailHeading = By.id("mentor-detail-h1-11");
    private final By bookSessionButton = By.id("book-session-btn");
    private final By bookingForm = By.id("booking-form");
    private final By topicInput = By.id("booking-topic");
    private final By dateInput = By.id("booking-date");
    private final By timeInput = By.id("booking-time");
    private final By durationSelect = By.id("booking-duration");
    private final By planTypeSelect = By.id("booking-plan-type");
    private final By occurrencesInput = By.id("booking-occurrences");
    private final By messageInput = By.id("booking-message");
    private final By bookingSubmitButton = By.id("booking-submit-btn");
    private final By bookingSuccess = By.id("mentor-detail-div-20");
    private final By bookingError = By.id("mentor-detail-div-59");
    private final By bookingErrors = By.cssSelector("#booking-form .err");
    private final By ratingNumber = By.id("mentor-detail-span-15");

    public MentorPage(WebDriver driver) {
        super(driver);
    }

    public MentorPage openMentorList() {
        driver.get(ConfigReader.baseUrl() + "/student/mentors");
        waitForEither(mentorCards, emptyMentorState);
        return this;
    }

    public boolean isMentorListLoaded() {
        return isDisplayed(mentorListPage) && (isPresent(mentorCards) || isDisplayed(emptyMentorState));
    }

    public int mentorCount() {
        return findAll(mentorCards).size();
    }

    public MentorPage searchBySkill(String skill) {
        type(searchSkillInput, skill);
        wait.documentReady();
        return this;
    }

    public MentorPage setAvailableOnly(boolean enabled) {
        boolean selected = wait.visible(availableOnlyCheckbox).isSelected();
        if (selected != enabled) {
            click(availableOnlyCheckbox);
        }
        return this;
    }

    public MentorPage openFirstMentorDetail() {
        wait.visible(mentorCards);
        click(mentorCards);
        wait.visible(detailPage);
        return this;
    }

    public boolean isDetailLoaded() {
        return isDisplayed(detailPage) && isDisplayed(detailHeading);
    }

    public String mentorName() {
        return text(detailHeading);
    }

    public String displayedRating() {
        return isDisplayed(ratingNumber) ? text(ratingNumber) : "";
    }

    public MentorPage openBookingForm() {
        click(bookSessionButton);
        wait.visible(bookingForm);
        return this;
    }

    public void bookSession(String topic, String date, String time, String planType, String occurrences, String message) {
        openBookingForm();
        type(topicInput, topic);
        type(dateInput, date);
        type(timeInput, time);
        new Select(wait.visible(planTypeSelect)).selectByValue(planType);
        if (!"SINGLE".equals(planType) && isDisplayed(occurrencesInput)) {
            type(occurrencesInput, occurrences);
        }
        new Select(wait.visible(durationSelect)).selectByValue("60");
        type(messageInput, message);
        click(bookingSubmitButton);
        waitForEither(bookingSuccess, bookingError);
    }

    public boolean isBookingSuccessDisplayed() {
        return isDisplayed(bookingSuccess);
    }

    public boolean isBookingSubmitDisabled() {
        openBookingForm();
        return !wait.visible(bookingSubmitButton).isEnabled();
    }

    public boolean hasBookingValidationError(String expectedText) {
        return driver.findElements(bookingErrors)
                .stream()
                .map(element -> element.getText().trim())
                .anyMatch(text -> text.toLowerCase().contains(expectedText.toLowerCase()));
    }

    public boolean invalidPastDateIsBlocked(String date, String time) {
        openBookingForm();
        type(topicInput, "Invalid date validation");
        type(dateInput, date);
        type(timeInput, time);
        return !wait.visible(bookingSubmitButton).isEnabled()
                || hasBookingValidationError("future")
                || hasBookingValidationError("date");
    }
}
