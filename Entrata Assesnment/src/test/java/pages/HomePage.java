package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;

public class HomePage {
    private static final Logger logger = LogManager.getLogger(HomePage.class);
    private WebDriver driver;
    private WebDriverWait wait;
    private JSONObject testData;
    private JSONObject setData;

    public HomePage(WebDriver driver, JSONObject testData, JSONObject setData) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.testData = testData;
        this.setData = setData;
        logger.info("Initialized HomePage");
    }

    public void verifyPageLoad() {
        try {
            logger.info("Verifying page load");
            // Check for HTTP errors
            Assert.assertNotEquals(driver.getTitle(), "404 Not Found", "Page not found (404 error)");
            Assert.assertNotEquals(driver.getTitle(), "500 Internal Server Error", "Server error (500)");
            logger.info("No HTTP errors detected");

            // Wait for page to be fully loaded
            wait.until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
            logger.info("Page fully loaded");

            // Verify expected title
            String actualTitle = driver.getTitle();
            String expectedTitle = testData.get("Title").toString();
            Assert.assertEquals(actualTitle, expectedTitle,
                    "Page title doesn't match. Actual: '" + actualTitle + "', Expected: '" + expectedTitle + "'");
            logger.info("Page title verified: ", actualTitle);
        } catch (Exception e) {
            logger.error("Page load verification failed: ", e.getMessage(), e);
            Assert.fail("Page failed to load properly: " + e.getMessage());
        }
    }

    public void verifyStatusCode() {
        String url = setData.get("website").toString();
        try {
            logger.info("Verifying status code for URL: ", url);
            URL targetUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();

            int statusCode = connection.getResponseCode();
            Assert.assertEquals(statusCode, 200, "Expected HTTP 200 but got: " + statusCode);
            logger.info("Status code verified: ", statusCode);

            connection.disconnect();
        } catch (IOException e) {
            logger.error("Status code verification failed: ", e.getMessage(), e);
            Assert.fail("Status check failed: " + e.getMessage());
        }
    }
}