package com.mentormatch.automation.base;

import com.mentormatch.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WaitUtils wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WaitUtils(driver);
    }

    protected void click(By locator) {
        WebElement element = wait.clickable(locator);
        scrollIntoView(element);
        element.click();
    }

    protected void type(By locator, String value) {
        WebElement element = wait.visible(locator);
        scrollIntoView(element);
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        element.sendKeys(Keys.BACK_SPACE);
        if (value != null && !value.isEmpty()) {
            element.sendKeys(value);
        }
    }

    protected String text(By locator) {
        return wait.visible(locator).getText().trim();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return wait.isVisible(locator, 3);
        } catch (RuntimeException e) {
            return false;
        }
    }

    protected boolean isPresent(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    protected List<WebElement> findAll(By locator) {
        return driver.findElements(locator);
    }

    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", element);
    }

    protected boolean waitForEither(By first, By second) {
        try {
            new WaitUtils(driver).documentReady();
            return wait.isVisible(first, 10) || wait.isVisible(second, 1);
        } catch (TimeoutException e) {
            return false;
        }
    }
}
