package pages;



import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;

import org.json.simple.JSONObject;

import org.openqa.selenium.By;

import org.openqa.selenium.JavascriptExecutor;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.Assert;



import java.time.Duration;



public class SignUpPage {

    private static final Logger logger = LogManager.getLogger(SignUpPage.class);

    private WebDriver driver;

    private WebDriverWait wait;

    private JSONObject testData;

    private JSONObject setData;

    public SignUpPage(WebDriver driver, JSONObject testData,JSONObject setData) {

        this.driver = driver;

        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        this.testData = testData;
        this.setData = setData;
        logger.info("Initialized SignUpFlowPage");

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

    public void clickSignUpButton() {

        try {

            navigateToSignInPage();

            logger.info("Waiting for page to be fully loaded");

            wait.until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));



            String signButtonXpath = testData.get("SignUpButtonXpath").toString();

            logger.info("Locating Sign Up button with XPath: {}", signButtonXpath);

            WebElement signUp = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(signButtonXpath)));



            Assert.assertTrue(signUp.isDisplayed(), "Sign Up button should be visible");

            Assert.assertTrue(signUp.isEnabled(), "Sign Up button should be enabled");



            logger.info("Pre-click validation: Is displayed: {}, Is enabled: {}", signUp.isDisplayed(), signUp.isEnabled());



            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", signUp);

            wait.until(ExpectedConditions.elementToBeClickable(signUp)).click();

            logger.info("Clicked Sign Up button. Current URL: {}", driver.getCurrentUrl());

        } catch (Exception e) {

            logger.error("Sign Up test failed: {}", e.getMessage(), e);

            Assert.fail("Sign Up test failed: " + e.getMessage());

        }

    }



    public void verifySignUpPage() {

        try {

            logger.info("Verifying Sign Up page load");

            wait.until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));



            String actualTitle = driver.getTitle();

            String expectedTitle = testData.get("SignUpTitle").toString();

            Assert.assertEquals(actualTitle, expectedTitle,

                    "Page title doesn't match. Actual: '" + actualTitle + "', Expected: '" + expectedTitle + "'");

            logger.info("Sign Up page verified. Title: {}", actualTitle);

        } catch (Exception e) {

            logger.error("Sign Up page verification failed: {}", e.getMessage(), e);

            Assert.fail("Sign Up page failed to load properly: " + e.getMessage());

        }

    }



    public void performPropertyManagerLogin() {

        try {

            logger.info("Waiting for Property Manager page to load");

            wait.until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));



            String propertyMLogin = testData.get("PropertyManagerLogin").toString();

            logger.info("Locating Property Manager button with XPath: {}", propertyMLogin);

            WebElement propertySignUp = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(propertyMLogin)));



            Assert.assertTrue(propertySignUp.isDisplayed(), "Property Manager Sign Up button should be visible");

            Assert.assertTrue(propertySignUp.isEnabled(), "Property Manager Sign Up button should be enabled");

            logger.info("Property Manager button validation passed");



            propertySignUp.click();

            logger.info("Clicked Property Manager button");



            wait.until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));



            WebElement field = driver.findElement(By.xpath("//input[@id='entrata-username']"));

            field.clear();

            JSONObject formData = (JSONObject) testData.get("DemoFormData");

            field.sendKeys(formData.get("Email").toString());

            logger.info("Entered email: {}", formData.get("Email").toString());

        } catch (Exception e) {

            logger.error("Property Manager login failed: {}", e.getMessage(), e);

            Assert.fail("Property Manager login failed: " + e.getMessage());

        }

    }



    public void verifyPropertyManagerPage() {

        try {

            logger.info("Verifying Property Manager page load");

            wait.until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));



            String actualTitle = driver.getTitle();

            String expectedTitle = testData.get("PropertyManagerPageTitle").toString();

            Assert.assertEquals(actualTitle, expectedTitle,

                    "Page title doesn't match. Actual: '" + actualTitle + "', Expected: '" + expectedTitle + "'");

            logger.info("Property Manager page verified. Title: {}", actualTitle);

        } catch (Exception e) {

            logger.error("Property Manager page verification failed: {}", e.getMessage(), e);

            Assert.fail("Property Manager page failed to load properly: " + e.getMessage());

        }

    }

}