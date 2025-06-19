# Selenium Test Automation Framework : Entrata Asssessment 

## Overview
This framework uses Selenium WebDriver, TestNG, Log4j, and Extent Reports to automate Entrata Web-application testing. It follows the Page Object Model (POM).

## Prerequisites
- Java JDK 17 or higher
- Maven 3.8.6 or higher
- Chrome Browser
- IDE (e.g., IntelliJ IDEA, Eclipse)

## Setup Instructions
1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd <repository-directory>
   ```

2. **Install Dependencies**
   Run the following command to download dependencies:
   ```bash
   mvn clean install
   ```

3. **Configure Test Data**
   - Place `Setup.json` and `Data.json` in the `src/Data` directory.
   - Ensure the JSON files contain valid test data (e.g., website URL, XPaths, form data).

4. **Setup Report Directories**
   - Created directories: `screenshots` and `extent-reports` in the project root.
   - Screenshots are saved in `screenshots/`.
   - Extent Reports are generated in `extent-reports/extent-report.html`.

## Execution Instructions
1. **Run Tests**
   Execute tests using Maven:
   ```bash
   mvn test
   ```

2. **View Reports**
   - Open `extent-reports/extent-report.html` in a browser to view the test report.
   - Check `logs/test.log` for detailed logs.
   - Screenshots for failed tests are saved in `screenshots/`.

## Framework Structure
- **/src/test/java/pages**: Page Object classes (`HomePage`, `SignUpFlowPage`, `WatchDemoPage`).
- **/src/test/java/tests**: Test classes (`TestAll`).
- **/src/test/java/utils**: Utility classes (`TestListener`, `ExtentReportManager`).
- **/src/Data**: Test data JSON files.
- **/src/resources**: Log4j configuration (`log4j2.xml`).
- **/reports**: Screenshots and Extent Reports.

## Features
- **Page Object Model**: Modular and reusable page classes.
- **Logging**: Log4j for detailed execution logs.
- **Screenshots**: Captured on test failure.
- **Extent Reports**: HTML reports with test results and screenshots.
- **TestNG**: Lifecycle hooks and listeners for setup, teardown, and reporting.

## Troubleshooting
- Ensure ChromeDriver is compatible with your Chrome version (WebDriverManager handles this automatically).
- Verify JSON files are correctly formatted.
- Contact the team for further assistance.