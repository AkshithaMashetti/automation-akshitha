package com.mentormatch.automation.base;

import com.mentormatch.automation.pages.DashboardPage;
import com.mentormatch.automation.pages.LoginPage;
import com.mentormatch.automation.pages.MentorPage;
import com.mentormatch.automation.pages.RegisterPage;
import com.mentormatch.automation.pages.ReviewPage;
import com.mentormatch.automation.pages.SessionPage;
import com.mentormatch.automation.utils.ConfigReader;
import com.mentormatch.automation.utils.ScreenshotUtils;
import com.mentormatch.automation.utils.TestDataGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;
import java.util.Map;

public abstract class BaseTest {

    private static final Logger LOGGER = LogManager.getLogger(BaseTest.class);
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    public record UserAccount(String fullName, String email, String password) {
    }

    public static WebDriver currentDriver() {
        return DRIVER.get();
    }

    protected WebDriver driver() {
        return DRIVER.get();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        WebDriver driver = createDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        try {
            driver.manage().window().maximize();
        } catch (RuntimeException ignored) {
            LOGGER.debug("Window maximize was skipped by the active driver.");
        }
        DRIVER.set(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            if (result.isSuccess() && ConfigReader.getBoolean("screenshotOnSuccess", false)) {
                ScreenshotUtils.capture(driver, result.getMethod().getMethodName() + "_success");
            }
            driver.quit();
            DRIVER.remove();
        }
    }

    protected void loginAsStudent() {
        new LoginPage(driver()).open()
                .login(ConfigReader.get("studentEmail"), ConfigReader.get("studentPassword"));
        new DashboardPage(driver()).waitForStudentLanding();
    }

    protected void loginAsMentor() {
        new LoginPage(driver()).open()
                .login(ConfigReader.get("mentorEmail"), ConfigReader.get("mentorPassword"));
        new DashboardPage(driver()).waitForMentorDashboard();
    }

    protected void loginAsAdmin() {
        new LoginPage(driver()).open()
                .login(ConfigReader.get("adminEmail"), ConfigReader.get("adminPassword"));
        new DashboardPage(driver()).waitForAdminDashboard();
    }

    protected void clearSession() {
        driver().manage().deleteAllCookies();
        ((JavascriptExecutor) driver()).executeScript("window.localStorage.clear(); window.sessionStorage.clear();");
    }

    protected String value(Map<String, String> data, String key, String fallback) {
        String value = data.get(key);
        return value == null || value.isBlank() ? fallback : value;
    }

    protected UserAccount registerStudent(boolean completeProfile) {
        UserAccount user = new UserAccount(
                TestDataGenerator.uniqueName("Automation Student"),
                TestDataGenerator.uniqueEmail("student"),
                "Student@1234"
        );
        new RegisterPage(driver()).open().register(user.fullName(), user.email(), user.password(), "STUDENT");
        new RegisterPage(driver()).waitForSuccess();
        new LoginPage(driver()).open().login(user.email(), user.password());
        DashboardPage dashboardPage = new DashboardPage(driver());
        dashboardPage.waitForStudentLanding();
        if (completeProfile) {
            dashboardPage.completeStudentProfileIfDisplayed(
                    "Automation learner",
                    "QA Engineer",
                    "Selenium, Java, TestNG",
                    "Build reliable MentorMatch automation."
            );
        }
        return user;
    }

    protected UserAccount registerMentor() {
        UserAccount user = new UserAccount(
                TestDataGenerator.uniqueName("Automation Mentor"),
                TestDataGenerator.uniqueEmail("mentor"),
                "Mentor@1234"
        );
        new RegisterPage(driver()).open().register(user.fullName(), user.email(), user.password(), "MENTOR");
        new RegisterPage(driver()).waitForSuccess();
        return user;
    }

    protected UserAccount createPendingSessionForNewStudent(String topic) {
        ensureMentorAvailability(true);
        UserAccount student = registerStudent(true);
        new MentorPage(driver()).openMentorList().openFirstMentorDetail()
                .bookSession(topic, TestDataGenerator.futureDate(2), TestDataGenerator.stableFutureTime(), "SINGLE", "1",
                        "Automation request for " + topic);
        clearSession();
        return student;
    }

    protected UserAccount createCompletedSessionForReview(String topic) {
        ensureMentorAvailability(true);
        UserAccount student = registerStudent(true);
        new MentorPage(driver()).openMentorList().openFirstMentorDetail()
                .bookSession(topic, TestDataGenerator.futureDate(2), TestDataGenerator.stableFutureTime(), "SINGLE", "1",
                        "Automation completed-session precondition");
        clearSession();

        loginAsMentor();
        SessionPage sessions = new SessionPage(driver()).openMentorSessions();
        sessions.acceptSessionByTopic(topic, TestDataGenerator.meetingLink());
        sessions.completeSessionByTopic(topic);
        clearSession();

        new LoginPage(driver()).open().login(student.email(), student.password());
        new DashboardPage(driver()).waitForStudentLanding();
        return student;
    }

    protected void submitReviewForCompletedSession(String topic, int rating, String comment) {
        SessionPage sessions = new SessionPage(driver()).openStudentSessions();
        sessions.openReviewForTopic(topic);
        new ReviewPage(driver()).submitReview(rating, comment);
    }

    protected void ensureMentorAvailability(boolean expectedAvailable) {
        clearSession();
        loginAsMentor();
        new DashboardPage(driver()).ensureMentorAvailability(expectedAvailable);
        clearSession();
    }

    private WebDriver createDriver() {
        String browser = ConfigReader.get("browser", "chrome").toLowerCase();
        boolean headless = ConfigReader.getBoolean("headless", false);

        return switch (browser) {
            case "edge" -> {
                EdgeOptions options = new EdgeOptions();
                options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                if (headless) {
                    options.addArguments("--headless=new");
                }
                options.addArguments("--disable-notifications", "--remote-allow-origins=*");
                yield new EdgeDriver(options);
            }
            case "chrome" -> {
                ChromeOptions options = new ChromeOptions();
                options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                if (headless) {
                    options.addArguments("--headless=new");
                }
                options.addArguments("--disable-notifications", "--remote-allow-origins=*");
                yield new ChromeDriver(options);
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };
    }
}
