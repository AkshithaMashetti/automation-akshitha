package com.mentormatch.automation.tests;

import com.mentormatch.automation.base.BaseTest;
import com.mentormatch.automation.data.MentorMatchDataProvider;
import com.mentormatch.automation.pages.DashboardPage;
import com.mentormatch.automation.pages.LoginPage;
import com.mentormatch.automation.pages.RegisterPage;
import com.mentormatch.automation.utils.ConfigReader;
import com.mentormatch.automation.utils.TestDataGenerator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class AuthTests extends BaseTest {

    @Test(groups = "smoke", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc01RegisterValidStudent(Map<String, String> data) {
        RegisterPage registerPage = new RegisterPage(driver()).open();
        registerPage.register(value(data, "fullName", TestDataGenerator.uniqueName("Automation Student")),
                TestDataGenerator.uniqueEmail("student"),
                value(data, "password", "Student@1234"),
                "STUDENT");
        Assert.assertTrue(registerPage.waitForSuccess(), "Student registration success message should be displayed.");
    }

    @Test(groups = "smoke", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc02RegisterValidMentor(Map<String, String> data) {
        RegisterPage registerPage = new RegisterPage(driver()).open();
        registerPage.register(value(data, "fullName", TestDataGenerator.uniqueName("Automation Mentor")),
                TestDataGenerator.uniqueEmail("mentor"),
                value(data, "password", "Mentor@1234"),
                "MENTOR");
        Assert.assertTrue(registerPage.waitForSuccess(), "Mentor registration success message should be displayed.");
    }

    @Test(groups = "smoke", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc03LoginValidUser(Map<String, String> data) {
        new LoginPage(driver()).open().login(value(data, "email", "student@mentormatch.com"), value(data, "password", "Student@1234"));
        DashboardPage dashboardPage = new DashboardPage(driver());
        dashboardPage.waitForStudentLanding();
        Assert.assertTrue(dashboardPage.isStudentDashboardDisplayed() || dashboardPage.isStudentProfileDisplayed(),
                "Valid student login should land on dashboard or profile completion page.");
    }

    @Test(groups = "regression", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc04LoginInvalidEmail(Map<String, String> data) {
        LoginPage loginPage = new LoginPage(driver()).open();
        loginPage.login(value(data, "email", "abc@"), value(data, "password", "Pass123"));
        Assert.assertTrue(loginPage.hasValidationMessage("valid email"), "Invalid email format should show client-side validation.");
    }

    //    @Test(groups = "regression", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
//    public void tc05LoginInvalidPassword(Map<String, String> data) {
//        LoginPage loginPage = new LoginPage(driver()).open();
//        loginPage.login(value(data, "email", "student@mentormatch.com"), value(data, "password", "Wrong123"));
//        Assert.assertTrue(loginPage.hasServerError(), "Wrong password should show server-side login failure.");
//    }
    @Test(groups = "regression", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc05LoginInvalidPassword(Map<String, String> data) {
        LoginPage loginPage = new LoginPage(driver()).open();

        String email = data.getOrDefault("email", "student@mentormatch.com");
        String password = data.getOrDefault("password", "Wrong123");

        // OVERRIDE: If Excel provides a password that triggers frontend validation,
        // forcefully replace it with one that will reach the server.
        if (password.length() < 6) {
            password = "Wrong123!";
        }

        loginPage.login(email, password);

        Assert.assertTrue(loginPage.hasServerError(), "Server error element should be displayed for invalid credentials.");
    }

    @Test(groups = "regression", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc06LoginEmptyFields(Map<String, String> data) {
        LoginPage loginPage = new LoginPage(driver()).open();
        loginPage.submitEmpty();
        Assert.assertTrue(loginPage.hasValidationMessage("Email is required"), "Email required validation should be shown.");
        Assert.assertTrue(loginPage.hasValidationMessage("Password is required"), "Password required validation should be shown.");
    }

    @Test(groups = "regression", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc07RegisterInvalidEmail(Map<String, String> data) {
        RegisterPage registerPage = new RegisterPage(driver()).open();
        registerPage.register(value(data, "fullName", "Invalid Email User"), value(data, "email", "abc@"),
                value(data, "password", "Student@1234"), "STUDENT");
        Assert.assertTrue(registerPage.hasValidationMessage("valid email"), "Invalid registration email should be rejected.");
    }

    @Test(groups = "regression", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc08RegisterShortPassword(Map<String, String> data) {
        RegisterPage registerPage = new RegisterPage(driver()).open();
        registerPage.register(value(data, "fullName", "Short Password User"), TestDataGenerator.uniqueEmail("shortpassword"),
                value(data, "password", "123"), "STUDENT");
        Assert.assertTrue(registerPage.hasValidationMessage("Minimum 6 characters"), "Short password validation should be displayed.");
    }

    @Test(groups = "regression", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc09RegisterEmptyForm(Map<String, String> data) {
        RegisterPage registerPage = new RegisterPage(driver()).open();
        registerPage.submitEmpty();
        Assert.assertTrue(registerPage.hasValidationMessage("required"), "Empty registration form should show required validations.");
    }

//    @Test(groups = "sanity", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
//    public void tc40UnauthorizedAccess(Map<String, String> data) {
//        clearSession();
//        driver().get(ConfigReader.baseUrl() + "/student/dashboard");
//        Assert.assertTrue(driver().getCurrentUrl().contains("/auth/login"), "Protected student dashboard should redirect anonymous users to login.");
//    }

    @Test(groups = "sanity", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc52EmailFieldEmptyValidation(Map<String, String> data) {
        LoginPage loginPage = new LoginPage(driver()).open();
        loginPage.enterPassword(value(data, "password", "Student@1234"));
        loginPage.submitEmpty();
        Assert.assertTrue(loginPage.hasValidationMessage("Email is required"), "Empty email should show validation.");
    }

    @Test(groups = "sanity", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc53PasswordFieldEmptyValidation(Map<String, String> data) {
        LoginPage loginPage = new LoginPage(driver()).open();
        loginPage.enterEmail(value(data, "email", "student@mentormatch.com"));
        loginPage.submitEmpty();
        Assert.assertTrue(loginPage.hasValidationMessage("Password is required"), "Empty password should show validation.");
    }

    @Test(groups = "sanity", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc54MaximumPasswordLength(Map<String, String> data) {
        RegisterPage registerPage = new RegisterPage(driver()).open();
        registerPage.register(TestDataGenerator.uniqueName("Max Password"), TestDataGenerator.uniqueEmail("maxpassword"),
                value(data, "password", TestDataGenerator.longString(256)), "STUDENT");
        Assert.assertFalse(registerPage.isSuccessMessageDisplayed(), "Password maximum length should be enforced.");
    }

    @Test(groups = "sanity", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc55MaximumFullNameLength(Map<String, String> data) {
        RegisterPage registerPage = new RegisterPage(driver()).open();
        registerPage.register(value(data, "fullName", TestDataGenerator.longString(256)), TestDataGenerator.uniqueEmail("maxfullname"),
                "Student@1234", "STUDENT");
        Assert.assertFalse(registerPage.isSuccessMessageDisplayed(), "Full name maximum length should be enforced.");
    }

    @Test(groups = "sanity", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc56UniqueEmailValidation(Map<String, String> data) {
        RegisterPage registerPage = new RegisterPage(driver()).open();
        registerPage.register("Duplicate Email User", value(data, "email", "student@mentormatch.com"), "Student@1234", "STUDENT");
        Assert.assertTrue(registerPage.hasValidationMessage("already") || !registerPage.serverErrorText().isBlank(),
                "Duplicate email should be rejected.");
    }
}
