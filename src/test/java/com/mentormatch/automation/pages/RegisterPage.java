package com.mentormatch.automation.pages;

import com.mentormatch.automation.base.BasePage;
import com.mentormatch.automation.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.Locale;

public class RegisterPage extends BasePage {

    private final By form = By.id("register-form");
    private final By fullNameInput = By.id("fullName");
    private final By emailInput = By.id("email");
    private final By passwordInput = By.id("password");
    private final By roleSelect = By.id("role");
    private final By submitButton = By.id("register-submit-btn");
    private final By serverError = By.id("register-error");
    private final By successMessage = By.id("register-success");
    private final By validationErrors = By.cssSelector("#register-form .error-text");

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    public RegisterPage open() {
        driver.get(ConfigReader.baseUrl() + "/auth/register");
        wait.visible(form);
        return this;
    }

    public void register(String fullName, String email, String password, String role) {
        type(fullNameInput, fullName);
        type(emailInput, email);
        type(passwordInput, password);
        selectRole(role);
        click(submitButton);
    }

    public void submitEmpty() {
        click(submitButton);
    }

    public void selectRole(String role) {
        if (role == null || role.isBlank()) {
            return;
        }
        String normalized = role.trim().toUpperCase(Locale.ROOT);
        if ("STUDENT".equals(normalized) || "MENTOR".equals(normalized)) {
            new Select(wait.visible(roleSelect)).selectByValue(normalized);
        } else {
            new Select(wait.visible(roleSelect)).selectByVisibleText(role);
        }
    }

    public boolean waitForSuccess() {
        return wait.isVisible(successMessage, 10);
    }

    public boolean isSuccessMessageDisplayed() {
        return isDisplayed(successMessage);
    }

    public String serverErrorText() {
        return isDisplayed(serverError) ? text(serverError) : "";
    }

    public String validationText() {
        return driver.findElements(validationErrors)
                .stream()
                .map(element -> element.getText().trim())
                .filter(value -> !value.isBlank())
                .reduce("", (left, right) -> left + " " + right)
                .trim();
    }

    public boolean hasValidationMessage(String message) {
        String combined = (validationText() + " " + serverErrorText()).toLowerCase(Locale.ROOT);
        return combined.contains(message.toLowerCase(Locale.ROOT));
    }
}
