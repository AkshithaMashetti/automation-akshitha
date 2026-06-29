package com.mentormatch.automation.data;

import com.mentormatch.automation.utils.ExcelUtils;
import com.mentormatch.automation.utils.TestDataGenerator;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MentorMatchDataProvider {

    private static final Pattern TEST_ID = Pattern.compile("(?i)(tc\\d{2})");

    private MentorMatchDataProvider() {
    }

    @DataProvider(name = "singleCase", parallel = false)
    public static Object[][] singleCase(Method method) {
        String testCaseId = testCaseId(method.getName());
        return new Object[][]{{dataFor(testCaseId)}};
    }

    @DataProvider(name = "invalidLoginData", parallel = false)
    public static Object[][] invalidLoginData() {
        return new Object[][]{
                {dataFor("TC04")},
                {dataFor("TC05")},
                {dataFor("TC06")}
        };
    }

    @DataProvider(name = "registrationValidationData", parallel = false)
    public static Object[][] registrationValidationData() {
        return new Object[][]{
                {dataFor("TC07")},
                {dataFor("TC08")},
                {dataFor("TC09")}
        };
    }

    @DataProvider(name = "reviewData", parallel = false)
    public static Object[][] reviewData() {
        return new Object[][]{
                {dataFor("TC41")},
                {dataFor("TC45")},
                {dataFor("TC46")}
        };
    }

    public static Map<String, String> dataFor(String testCaseId) {
        Map<String, String> data = new LinkedHashMap<>(defaultsFor(testCaseId));
        data.putAll(ExcelUtils.testDataForCase(testCaseId));
        data.put("testCaseId", testCaseId);
        return data;
    }

    private static String testCaseId(String methodName) {
        Matcher matcher = TEST_ID.matcher(methodName);
        if (matcher.find()) {
            return matcher.group(1).toUpperCase(Locale.ROOT);
        }
        return "TC00";
    }

    private static Map<String, String> defaultsFor(String testCaseId) {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("email", "");
        data.put("password", "Student@1234");
        data.put("fullName", "Automation User");
        data.put("role", "STUDENT");
        data.put("topic", TestDataGenerator.uniqueTopic("MentorMatch Session"));
        data.put("message", "Automation booking request");
        data.put("reason", "Automation validation reason");
        data.put("meetingLink", TestDataGenerator.meetingLink());
        data.put("rating", "5");
        data.put("comment", "Helpful mentoring session.");
        data.put("expectedMessage", "");

        switch (testCaseId) {
            case "TC01" -> {
                data.put("email", TestDataGenerator.uniqueEmail("student"));
                data.put("fullName", TestDataGenerator.uniqueName("Automation Student"));
                data.put("role", "STUDENT");
            }
            case "TC02" -> {
                data.put("email", TestDataGenerator.uniqueEmail("mentor"));
                data.put("fullName", TestDataGenerator.uniqueName("Automation Mentor"));
                data.put("password", "Mentor@1234");
                data.put("role", "MENTOR");
            }
            case "TC03" -> {
                data.put("email", "student@mentormatch.com");
                data.put("password", "Student@1234");
            }
            case "TC04" -> {
                data.put("email", "abc@");
                data.put("password", "Pass123");
                data.put("expectedMessage", "Enter a valid email");
            }
            case "TC05" -> {
                data.put("email", "student@mentormatch.com");
                data.put("password", "Wrong123");
                data.put("expectedMessage", "Login failed");
            }
            case "TC06", "TC09" -> data.put("expectedMessage", "required");
            case "TC07" -> {
                data.put("email", "invalid@");
                data.put("password", "Student@1234");
                data.put("expectedMessage", "Enter a valid email");
            }
            case "TC08" -> {
                data.put("email", TestDataGenerator.uniqueEmail("shortpassword"));
                data.put("password", "123");
                data.put("expectedMessage", "Minimum 6 characters");
            }
            case "TC28" -> data.put("meetingLink", "not-a-valid-url");
            case "TC41", "TC45" -> {
                data.put("rating", "5");
                data.put("comment", "Clear, practical and very helpful.");
            }
            case "TC43" -> {
                data.put("rating", "5");
                data.put("comment", TestDataGenerator.longString(1000));
            }
            case "TC46" -> {
                data.put("rating", "4");
                data.put("comment", "");
            }
            case "TC52" -> {
                data.put("password", "Student@1234");
                data.put("expectedMessage", "Email is required");
            }
            case "TC53" -> {
                data.put("email", "student@mentormatch.com");
                data.put("password", "");
                data.put("expectedMessage", "Password is required");
            }
            case "TC54" -> {
                data.put("email", TestDataGenerator.uniqueEmail("maxpassword"));
                data.put("password", TestDataGenerator.longString(256));
                data.put("expectedMessage", "maximum");
            }
            case "TC55" -> {
                data.put("email", TestDataGenerator.uniqueEmail("maxfullname"));
                data.put("fullName", TestDataGenerator.longString(256));
                data.put("expectedMessage", "maximum");
            }
            case "TC56" -> {
                data.put("email", "student@mentormatch.com");
                data.put("expectedMessage", "already");
            }
            default -> {
            }
        }
        return data;
    }
}
