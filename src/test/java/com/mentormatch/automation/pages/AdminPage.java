package com.mentormatch.automation.pages;

import com.mentormatch.automation.base.BasePage;
import com.mentormatch.automation.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AdminPage extends BasePage {

    private final By studentsPage = By.id("students-div-1");
    private final By searchInput = By.id("students-search-input");
    private final By loginForm = By.id("login-form");

    public AdminPage(WebDriver driver) {
        super(driver);
    }

    public AdminPage openStudents() {
        driver.get(ConfigReader.baseUrl() + "/admin/dashboard/students");
        if (waitForEither(studentsPage, loginForm) && isDisplayed(loginForm)) {
            throw new IllegalStateException("Admin session was not active. Current URL: " + driver.getCurrentUrl());
        }
        wait.visible(studentsPage);
        return this;
    }

    public void searchStudent(String email) {
        type(searchInput, email);
        wait.documentReady();
    }

    public void toggleStudentStatus(String email) {
        searchStudent(email);
        WebElement row = wait.visible(studentRow(email));
        WebElement button = row.findElement(By.cssSelector("button.toggle-btn"));
        scrollIntoView(button);
        button.click();
    }

    public boolean isStudentInactive(String email) {
        searchStudent(email);
        By inactiveBadge = By.xpath("//tr[.//td[contains(normalize-space()," + xpathLiteral(email) + ")]]//span[contains(@class,'status-badge') and normalize-space()='Inactive']");
        return wait.isVisible(inactiveBadge, 10);
    }

    private By studentRow(String email) {
        return By.xpath("//tr[.//td[contains(normalize-space()," + xpathLiteral(email) + ")]]");
    }

    private String xpathLiteral(String value) {
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        return "\"" + value + "\"";
    }
}
