package com.mentormatch.automation.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

public final class TestDataGenerator {

    private static final DateTimeFormatter DATE = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm");

    private TestDataGenerator() {
    }

    public static String uniqueEmail(String prefix) {
        String token = UUID.randomUUID().toString().replace("-", "").substring(0, 10).toLowerCase(Locale.ROOT);
        return prefix.toLowerCase(Locale.ROOT) + "." + token + "@gmail.com";
    }

    public static String uniqueName(String prefix) {
        String token = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        return prefix + " " + token;
    }

    public static String uniqueTopic(String prefix) {
        String token = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return prefix + " " + token;
    }

    public static String futureDate(int daysFromToday) {
        return LocalDate.now().plusDays(daysFromToday).format(DATE);
    }

    public static String pastDate(int daysBeforeToday) {
        return LocalDate.now().minusDays(daysBeforeToday).format(DATE);
    }

    public static String stableFutureTime() {
        return LocalTime.of(10, 30).format(TIME);
    }

    public static String meetingLink() {
        return "https://meet.google.com/" + UUID.randomUUID().toString().substring(0, 8);
    }

    public static String longString(int length) {
        return "A".repeat(Math.max(1, length));
    }
}
