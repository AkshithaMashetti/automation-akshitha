package com.mentormatch.automation.pages;

import com.mentormatch.automation.base.BasePage;
import com.mentormatch.automation.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By emailInput = By.id("email");
    private final By passwordInput = By.id("password");
    private final By submitButton = By.id("login-submit-btn");
    private final By form = By.id("login-form");
    private final By serverError = By.id("login-error");
    private final By validationErrors = By.cssSelector("#login-form .error-text");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        driver.get(ConfigReader.baseUrl() + "/auth/login");
        wait.visible(form);
        return this;
    }

    public void login(String email, String password) {
        type(emailInput, email);
        type(passwordInput, password);
        click(submitButton);
    }

    public void submitEmpty() {
        click(submitButton);
    }

    public void enterEmail(String email) {
        type(emailInput, email);
    }

    public void enterPassword(String password) {
        type(passwordInput, password);
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
        return validationText().toLowerCase().contains(message.toLowerCase());
    }

    public boolean hasServerError() {
        return isDisplayed(serverError) && !serverErrorText().isBlank();
    }

    public boolean isAtLoginPage() {
        return isDisplayed(form) || driver.getCurrentUrl().contains("/auth/login");
    }
}
