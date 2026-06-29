# MentorMatch Selenium Automation Framework

This folder is a standalone Selenium WebDriver + Java + TestNG hybrid framework for MentorMatch.

## Stack

- Java 17
- Selenium WebDriver
- TestNG groups
- Maven
- Hybrid design: Page Object Model + DataProvider/Excel + utilities
- Screenshot capture on failure
- Retry support
- Parallel execution ready through ThreadLocal WebDriver

## Test Class Organization

Tests are organized by MentorMatch module, not by suite type:

```text
AuthTests.java
DashboardTests.java
ProfileTests.java
MentorTests.java
SessionTests.java
NotificationTests.java
ReviewTests.java
AdminTests.java
```

Suite execution is still controlled through TestNG groups on each method:

```java
@Test(groups = "smoke")
@Test(groups = "sanity")
@Test(groups = "regression")
```

## Run

From `automation-tests`:

```powershell
mvn clean test
mvn clean test -DsuiteXmlFile=testng-smoke.xml
mvn clean test -DsuiteXmlFile=testng-sanity.xml
mvn clean test -DsuiteXmlFile=testng-regression.xml
```

Useful overrides:

```powershell
mvn clean test -Dbrowser=edge -Dheadless=true -DbaseUrl=https://mentormatch-green.netlify.app/
```

## Open In Eclipse

Do not choose `automation-tests` as the Eclipse workspace folder. Use any normal Eclipse workspace, then import this folder as a project:

1. `File` > `Import`.
2. Select `Maven` > `Existing Maven Projects`.
3. Browse to:

```text
C:\Users\2492376\Downloads\mentormatch-main\mentormatch-main\automation-tests
```

4. Select `pom.xml`.
5. Click `Finish`.

If Eclipse asks for a JRE, choose Java 17 or newer.

If you accidentally imported a project named `src`, remove it from Eclipse only:

1. Right-click `src` in Project Explorer.
2. Choose `Delete`.
3. Uncheck `Delete project contents on disk`.
4. Re-import `automation-tests` using `Maven > Existing Maven Projects`.

If Maven dependencies show a certificate/PKIX error in Eclipse, set this JVM argument for Eclipse/Maven:

```text
-Djavax.net.ssl.trustStoreType=Windows-ROOT
```

In Eclipse this is usually under `Window` > `Preferences` > `Java` > `Installed JREs` > selected JDK > `Edit` > `Default VM arguments`.

## Data

The framework looks for Excel test data at:

```text
src/test/resources/testdata/MentorMatch_Testing.xlsx
```

It reads the `TEST CASES` sheet and parses the `Test Data` column. If a row or column is missing, each test uses safe built-in defaults from `MentorMatchDataProvider`.

Seed credentials from the app initializer:

```text
student@mentormatch.com / Student@1234
mentor@mentormatch.com  / Mentor@1234
admin@mentormatch.com   / Admin@1234
```

Screenshots are written to `screenshots/`.
