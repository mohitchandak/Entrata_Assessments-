package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.SignUpPage;
import pages.WatchDemoPage;

import java.io.FileReader;
import java.io.IOException;

@Listeners(utils.TestListener.class)
public class TestAll {
    private static final Logger logger = LogManager.getLogger(TestAll.class);
    protected ChromeDriver driver;
    private static JSONObject testData, setData;

    private static void loadTestData() {
        JSONParser parser = new JSONParser();
        try {
            Object setup = parser.parse(new FileReader("src/Data/Setup.json"));
            Object data = parser.parse(new FileReader("src/Data/Data.json"));
            setData = (JSONObject) setup;
            testData = (JSONObject) data;
            logger.info("Test data loaded successfully");
        } catch (IOException | ParseException e) {
            logger.error("Failed to load test data: {}", e.getMessage(), e);
            throw new RuntimeException("Test data loading failed", e);
        }
    }

    @BeforeClass
    public void setup() {
        logger.info("Starting test setup");
        loadTestData();
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get(setData.get("website").toString());
        logger.info("Browser launched and navigated to: {}", setData.get("website"));
    }

    @Test(priority = 1)
    public void verifyPageLoad() {
        logger.info("Executing verifyPageLoad test");
        HomePage homePage = new HomePage(driver, testData, setData);
        homePage.verifyPageLoad();
    }

    @Test(priority = 2)
    public void verifyStatusCode() {
        logger.info("Executing verifyStatusCode test");
        HomePage homePage = new HomePage(driver, testData, setData);
        homePage.verifyStatusCode();
    }

    @Test(priority = 3)
    public void signUp() {
        logger.info("Executing signUp test");
        SignUpPage signUpFlowPage = new SignUpPage(driver, testData ,setData);
        signUpFlowPage.clickSignUpButton();
    }

    @Test(priority = 4, dependsOnMethods = "signUp")
    public void signUpPage() {
        logger.info("Executing signUpPage test");
        SignUpPage signUpFlowPage = new SignUpPage(driver, testData,setData);
        signUpFlowPage.verifySignUpPage();
    }

    @Test(priority = 5, dependsOnMethods = "signUpPage")
    public void propertyManagerLogin() {
        logger.info("Executing propertyManagerLogin test");
        SignUpPage signUpFlowPage = new SignUpPage(driver, testData,setData);
        signUpFlowPage.performPropertyManagerLogin();
    }

    @Test(priority = 6, dependsOnMethods = "propertyManagerLogin")
    public void propertyManagerPage() {
        logger.info("Executing propertyManagerPage test");
        SignUpPage signUpFlowPage = new SignUpPage(driver, testData,setData);
        signUpFlowPage.verifyPropertyManagerPage();
    }

    @Test(priority = 7)
    public void watchDemo() {
        logger.info("Executing watchDemo test");
        WatchDemoPage watchDemoPage = new WatchDemoPage(driver, testData,setData);
        watchDemoPage.clickWatchDemoButton();
    }

    @Test(priority = 8, dependsOnMethods = "watchDemo")
    public void demoPage() {
        logger.info("Executing demoPage test");
        WatchDemoPage watchDemoPage = new WatchDemoPage(driver, testData,setData);
        watchDemoPage.verifyDemoPage();
    }

    @Test(priority = 9, dependsOnMethods = "demoPage")
    public void testFillAndSubmitDemoForm() {
        logger.info("Executing testFillAndSubmitDemoForm test");
        WatchDemoPage watchDemoPage = new WatchDemoPage(driver, testData,setData);
        watchDemoPage.fillAndSubmitDemoForm();
    }

    @AfterClass
    public void tearDown() {
        logger.info("Starting test teardown");
        if (driver != null) {
            driver.quit();
            logger.info("Browser closed");
        }
    }
}