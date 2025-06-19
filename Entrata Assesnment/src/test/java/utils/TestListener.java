package utils;

import com.aventstack.extentreports.MediaEntityBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import tests.TestAll;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestListener implements ITestListener {
    private static final Logger logger = LogManager.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        ExtentReportManager.initExtentReports();
        logger.info("Test Suite started: ", context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentReportManager.startTest(result.getMethod().getMethodName());
        logger.info("Test started: ", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReportManager.getTest().pass("Test passed");
        logger.info("Test passed: ", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentReportManager.getTest().fail(result.getThrowable());
        logger.error("Test failed: ", result.getMethod().getMethodName(), result.getThrowable());

        // Capture screenshot
        Object testInstance = result.getInstance();
        if (testInstance instanceof TestAll) {
            try {
                Field driverField = TestAll.class.getDeclaredField("driver");
                driverField.setAccessible(true);
                WebDriver driver = (WebDriver) driverField.get(testInstance);
                if (driver != null) {
                    String screenshotPath = captureScreenshot(driver, result.getMethod().getMethodName());
                    ExtentReportManager.getTest().fail("Screenshot captured",
                            MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                    logger.info("Screenshot captured at: ", screenshotPath);
                }
            } catch (NoSuchFieldException | IllegalAccessException | IOException e) {
                logger.error("Failed to capture screenshot: ", e.getMessage(), e);
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentReportManager.getTest().skip("Test skipped");
        logger.warn("Test skipped: ", result.getMethod().getMethodName());
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.flushReports();
        logger.info("Test Suite finished: ", context.getName());
    }

    private String captureScreenshot(WebDriver driver, String testName) throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String screenshotName = testName + "_" + timestamp + ".png";
        String screenshotPath = "screenshots/" + screenshotName;

        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File destinationFile = new File(screenshotPath);
        FileUtils.copyFile(screenshotFile, destinationFile);

        return screenshotName; // Relative path for Extent Reports
    }
}