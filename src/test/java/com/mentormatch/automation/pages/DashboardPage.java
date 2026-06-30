package com.mentormatch.automation.pages;

import com.mentormatch.automation.base.BasePage;
import com.mentormatch.automation.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Locale;

public class DashboardPage extends BasePage {

    //    private final By studentDashboard = By.id("student-dashboard-div-2");
//    private final By studentProfileForm = By.id("student-profile-form-11");
//    private final By dashboardBookButton = By.id("dashboard-book-btn");
    private final By studentDashboard = By.cssSelector("[id^='student-dashboard-div']");
    private final By studentProfileForm = By.cssSelector("[id^='student-profile-form']");
    private final By dashboardBookButton = By.id("dashboard-book-btn");
    private final By totalSessionsStat = By.cssSelector("#stat-total-sessions .stat-val");
    private final By upcomingStat = By.cssSelector("#stat-upcoming .stat-val");
    private final By pendingStat = By.cssSelector("#stat-pending .stat-val");
    private final By completedStat = By.cssSelector("#stat-completed .stat-val");

    private final By headlineInput = By.id("headline");
    private final By currentRoleInput = By.id("currentRole");
    private final By interestsInput = By.id("student-profile-input-29");
    private final By goalsInput = By.id("goals");
    private final By saveProfileButton = By.id("save-profile-btn");
    // private final By profileSuccess = By.id("student-profile-div-39");
    private final By profileSuccess = By.cssSelector("[id^='student-profile-div']");
    private final By deleteProfileButton = By.id("delete-profile-btn");

    // private final By mentorDashboard = By.id("mentor-dashboard-div-44");
    private final By mentorSessionsTab = By.id("mentor-dashboard-button-13");
    private final By mentorReviewsTab = By.id("mentor-dashboard-button-18");
    private final By mentorProfileTab = By.id("mentor-dashboard-button-22");
    private final By mentorNotificationsTab = By.id("mentor-dashboard-button-26");
    private final By mentorAvailabilityToggle = By.id("mentor-dashboard-button-54");
    private final By mentorIndustryInput = By.id("industry");
    private final By mentorHourlyRateInput = By.id("hourlyRate");
    private final By mentorBioInput = By.id("bio");
    private final By mentorSkillInput = By.id("mentor-dashboard-input-182");
    private final By mentorSaveButton = By.id("mentor-dashboard-button-185");
    // private final By mentorSaveSuccess = By.id("mentor-dashboard-div-93");
    // private final By adminDashboard = By.id("admin-dashboard-div-1");
    private final By mentorDashboard = By.cssSelector("[id^='mentor-dashboard-div']");
    private final By mentorSaveSuccess = By.cssSelector("[id^='mentor-dashboard-div']"); // Adjust if this shares the same prefix as dashboard
    private final By adminDashboard = By.cssSelector("[id^='admin-dashboard-div']");
    private final By pending = By.id("mentor-dashboard-span-60");
    private final By active = By.id("mentor-dashboard-span-64");
    private final By completed = By.id("mentor-dashboard-span-68");
    private final By avg = By.id("mentor-dashboard-span-72");

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public void waitForStudentLanding() {
        waitForEither(studentDashboard, studentProfileForm);
    }

    public void waitForMentorDashboard() {
        wait.visible(mentorDashboard);
    }

    public void waitForAdminDashboard() {
        wait.visible(adminDashboard);
    }

    public boolean isStudentDashboardDisplayed() {
        return isDisplayed(studentDashboard);
    }

    public boolean isStudentProfileDisplayed() {
        return isDisplayed(studentProfileForm);
    }

    public boolean isMentorDashboardDisplayed() {
        return isDisplayed(mentorDashboard);
    }

    public void clickBookSession() {
        click(dashboardBookButton);
        wait.urlContains("/student/mentors");
    }

    public void openStudentProfile() {
        driver.get(ConfigReader.baseUrl() + "/student/profile");
        wait.visible(studentProfileForm);
    }

    public void completeStudentProfileIfDisplayed(String headline, String currentRole, String interests, String goals) {
        if (!isStudentProfileDisplayed()) {
            return;
        }
        completeStudentProfile(headline, currentRole, interests, goals);
    }

    public void completeStudentProfile(String headline, String currentRole, String interests, String goals) {
        wait.visible(studentProfileForm);
        type(headlineInput, headline);
        type(currentRoleInput, currentRole);
        if (interests != null && !interests.isBlank()) {
            type(interestsInput, interests);
            driver.switchTo().activeElement().sendKeys(",");
        }
        type(goalsInput, goals);
        click(saveProfileButton);
        waitForEither(studentDashboard, profileSuccess);
    }

    public void deleteStudentProfile() {
        click(deleteProfileButton);
        driver.switchTo().alert().accept();
        wait.urlContains("/auth/login");
    }

    public boolean dashboardStatsAreNumeric() {
        List<By> stats = List.of(pending, completed, active, avg);
        return stats.stream()
                .map(this::text)
                .allMatch(value -> value.matches("\\d+(\\.\\d+)?"));
    }

    public DashboardPage switchMentorTab(String tab) {
        String normalized = tab.toLowerCase(Locale.ROOT);
        switch (normalized) {
            case "sessions" -> click(mentorSessionsTab);
            case "reviews" -> click(mentorReviewsTab);
            case "profile" -> click(mentorProfileTab);
            case "notifications" -> click(mentorNotificationsTab);
            default -> throw new IllegalArgumentException("Unsupported mentor tab: " + tab);
        }
        return this;
    }

    public void ensureMentorAvailability(boolean expectedAvailable) {
        wait.visible(mentorAvailabilityToggle);
        boolean currentlyAvailable = text(mentorAvailabilityToggle).toLowerCase(Locale.ROOT).contains("available")
                && !text(mentorAvailabilityToggle).toLowerCase(Locale.ROOT).contains("unavailable");
        if (currentlyAvailable != expectedAvailable) {
            click(mentorAvailabilityToggle);
            wait.textContains(mentorAvailabilityToggle, expectedAvailable ? "Available" : "Unavailable");
        }
    }

    public void updateMentorProfile(String industry, String hourlyRate, String bio, String skill) {
        switchMentorTab("profile");
        wait.visible(mentorIndustryInput);
        type(mentorIndustryInput, industry);
        type(mentorHourlyRateInput, hourlyRate);
        type(mentorBioInput, bio);
        if (skill != null && !skill.isBlank()) {
            type(mentorSkillInput, skill);
            driver.switchTo().activeElement().sendKeys(",");
        }
        click(mentorSaveButton);
        wait.visible(mentorSaveSuccess);
    }

    public boolean mentorProfileSaved() {
        return isDisplayed(mentorSaveSuccess);
    }
}
