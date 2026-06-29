package com.mentormatch.automation.pages;

import com.mentormatch.automation.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ReviewPage extends BasePage {

    private final By reviewPage = By.id("submit-review-div-2");
    private final By commentInput = By.id("comment");
    private final By submitButton = By.id("review-submit-btn");
    private final By successMessage = By.id("submit-review-div-8");
    private final By errorMessage = By.id("submit-review-div-9");

    public ReviewPage(WebDriver driver) {
        super(driver);
    }

    public ReviewPage waitForLoad() {
        wait.visible(reviewPage);
        return this;
    }

    public void selectRating(int rating) {
        waitForLoad();
        click(By.id("star-" + rating));
    }

    public void enterComment(String comment) {
        type(commentInput, comment == null ? "" : comment);
    }

    public void submit() {
        click(submitButton);
    }

    public void submitReview(int rating, String comment) {
        selectRating(rating);
        enterComment(comment);
        submit();
        waitForEither(successMessage, errorMessage);
    }

    public boolean isSuccessDisplayed() {
        return isDisplayed(successMessage);
    }

    public String errorText() {
        return isDisplayed(errorMessage) ? text(errorMessage) : "";
    }

    public boolean isSubmitDisabled() {
        waitForLoad();
        return !wait.visible(submitButton).isEnabled();
    }
}
