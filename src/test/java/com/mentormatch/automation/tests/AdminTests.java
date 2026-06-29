package com.mentormatch.automation.tests;

import com.mentormatch.automation.base.BaseTest;
import com.mentormatch.automation.data.MentorMatchDataProvider;
import com.mentormatch.automation.pages.AdminPage;
import com.mentormatch.automation.pages.RegisterPage;
import com.mentormatch.automation.utils.TestDataGenerator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class AdminTests extends BaseTest {

    @Test(groups = "sanity", dataProvider = "singleCase", dataProviderClass = MentorMatchDataProvider.class)
    public void tc13DeactivateStudentProfile(Map<String, String> data) {
        UserAccount user = new UserAccount(TestDataGenerator.uniqueName("Deactivate Student"),
                TestDataGenerator.uniqueEmail("deactivate"), "Student@1234");
        RegisterPage registerPage = new RegisterPage(driver()).open();
        registerPage.register(user.fullName(), user.email(), user.password(), "STUDENT");
        Assert.assertTrue(registerPage.waitForSuccess(), "Student precondition registration should succeed before admin deactivation.");

        loginAsAdmin();
        AdminPage adminPage = new AdminPage(driver()).openStudents();
        adminPage.toggleStudentStatus(user.email());
        Assert.assertTrue(adminPage.isStudentInactive(user.email()), "Admin should deactivate a student account.");
    }
}
