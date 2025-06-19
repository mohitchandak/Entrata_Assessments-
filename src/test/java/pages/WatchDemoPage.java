package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class WatchDemoPage {
    private static final Logger logger = LogManager.getLogger(WatchDemoPage.class);
    private WebDriver driver;
    private WebDriverWait wait;
    private JSONObject testData;
    private JSONObject setData;
    public WatchDemoPage(WebDriver driver, JSONObject testData,JSONObject setData) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.testData = testData;
        this.setData = setData;
        logger.info("Initialized WatchDemoPage");
    }
    private void navigateToSignInPage() {
        try {
            String signInUrl = setData.get("website").toString();
            String currentUrl = driver.getCurrentUrl();
            logger.info("Checking current URL: {}", currentUrl);

            if (!currentUrl.equals(signInUrl)) {
                logger.info("Navigating to sign-in page: {}", signInUrl);
                driver.get(signInUrl);
                wait.until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
                logger.info("Navigated to sign-in page successfully");
            } else {
                logger.info("Already on sign-in page: {}", signInUrl);
            }
        } catch (Exception e) {
            logger.error("Failed to navigate to sign-in page: {}", e.getMessage(), e);
            Assert.fail("Navigation to sign-in page failed: " + e.getMessage());
        }
    }
    public void clickWatchDemoButton() {
        navigateToSignInPage();
        try {
            logger.info("Waiting for page to be fully loaded");
            wait.until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));

            String watchDemoButtonXpath = testData.get("WatchDemoButtonXpath").toString();
            logger.info("Locating Watch Demo button with XPath: {}", watchDemoButtonXpath);
            WebElement watchDemoButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(watchDemoButtonXpath)));

            Assert.assertTrue(watchDemoButton.isDisplayed(), "Watch Demo button should be visible");
            Assert.assertTrue(watchDemoButton.isEnabled(), "Watch Demo button should be enabled");
            logger.info("Watch Demo button validation: Is displayed: {}, Is enabled: {}", watchDemoButton.isDisplayed(), watchDemoButton.isEnabled());

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", watchDemoButton);
            wait.until(ExpectedConditions.elementToBeClickable(watchDemoButton)).click();
            logger.info("Clicked Watch Demo button");
        } catch (Exception e) {
            logger.error("Watch Demo button click failed: {}", e.getMessage(), e);
            Assert.fail("Watch Demo test failed: " + e.getMessage());
        }
    }

    public void verifyDemoPage() {
        try {
            logger.info("Verifying Demo page load");
            wait.until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));

            String actualTitle = driver.getTitle();
            String expectedTitle = testData.get("WatchDemoTitle").toString();
            Assert.assertEquals(actualTitle, expectedTitle,
                    "Page title doesn't match. Actual: '" + actualTitle + "', Expected: '" + expectedTitle + "'");
            logger.info("Demo page verified. Title: {}", actualTitle);
        } catch (Exception e) {
            logger.error("Demo page verification failed: {}", e.getMessage(), e);
            Assert.fail("Demo page failed to load properly: " + e.getMessage());
        }
    }

    public void fillAndSubmitDemoForm() {
        try {
            logger.info("Filling Demo form");
            JSONObject formData = (JSONObject) testData.get("DemoFormData");

            fillFormField("FirstName", formData.get("FirstName").toString());
            fillFormField("LastName", formData.get("LastName").toString());
            fillFormField("Email", formData.get("Email").toString());
            fillFormField("Company", formData.get("Company").toString());
            fillFormField("Phone", formData.get("Phone").toString());
            selectDropdownOption("Unit_Count__c", formData.get("UnitCount").toString());
            fillFormField("Title", formData.get("JobTitle").toString());
            selectDropdownOption("demoRequest", formData.get("DemoRequest").toString());

            Thread.sleep(2000); // Consider replacing with a wait condition
            logger.info("Demo form filled successfully");
        } catch (Exception e) {
            logger.error("Demo form filling failed: {}", e.getMessage(), e);
            Assert.fail("Demo form filling failed: " + e.getMessage());
        }
    }

    private void fillFormField(String fieldId, String value) {
        try {
            logger.info("Filling field {} with value: {}", fieldId, value);
            WebElement field = driver.findElement(By.xpath("//input[@id='" + fieldId + "']"));
            field.clear();
            field.sendKeys(value);
            Assert.assertEquals(field.getAttribute("value"), value, fieldId + " field value mismatch");
            logger.info("Field {} filled successfully", fieldId);
        } catch (Exception e) {
            logger.error("Failed to fill field {}: {}", fieldId, e.getMessage(), e);
            throw e;
        }
    }

    private void selectDropdownOption(String selectId, String value) {
        try {
            logger.info("Selecting dropdown option for {} with value: {}", selectId, value);
            WebElement selectElement = driver.findElement(By.xpath("//select[@id='" + selectId + "']"));
            Select dropdown = new Select(selectElement);
            dropdown.selectByVisibleText(value);
            Assert.assertEquals(dropdown.getFirstSelectedOption().getText(), value, selectId + " selection mismatch");
            logger.info("Dropdown {} selected successfully", selectId);
        } catch (Exception e) {
            logger.error("Failed to select dropdown {}: {}", selectId, e.getMessage(), e);
            throw e;
        }
    }
}