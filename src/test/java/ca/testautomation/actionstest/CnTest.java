package ca.testautomation.actionstest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class CnTest {
    WebDriver driver;
    WebDriverWait wait;
    Actions actions;

    @BeforeSuite
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        actions = new Actions(driver);
    }

    @BeforeMethod
    public void setUpTest() {
        driver.manage().window().maximize();
        driver.get("https://cn.ca/en/");
    }

    @Test
    public void tc1() {
        WebElement service = driver.findElement(By.id("ctl06__10941b240573643_repMainNav_topLevelLink_1"));
        WebElement trucking = driver.findElement(By.id("ctl06__10941b240573643_repMainNav_repSecondLevel_1_secondLevelLink_2"));
        WebElement express = driver.findElement(By.id("ctl06__10941b240573643_repMainNav_repSecondLevel_1_repThirdLevel_2_thirdLevelLink_1"));
        WebElement jobAid = driver.findElement(By.id("ctl06__10941b240573643_repMainNav_repSecondLevel_1_repThirdLevel_2_repFourthLevel_1_fourthLevelLink_0"));

        actions.moveToElement(service).pause(1000)
                .moveToElement(trucking).pause(1000)
                .moveToElement(express).pause(1000)
                .moveToElement(jobAid)
                .click().build().perform();

        Assert.assertEquals(driver.getTitle(), "Job Aid Gallery | cn.ca");
    }

    @Test
    public void tc2() {
        WebElement service = driver.findElement(By.id("ctl06__10941b240573643_repMainNav_topLevelLink_1"));

        // Opens the service page on a new tab
        // .pause(3000) is an inline alternative to a separate line of Thread.sleep(3000)
        actions.moveToElement(service).keyDown(Keys.CONTROL).click().pause(3000).build().perform();

        Assert.assertEquals(driver.getWindowHandles().size(), 2);
    }

    @Test
    public void tc3() {
        // web element exists in DOM
        WebElement jobAid = driver.findElement(By.id("ctl06__10941b240573643_repMainNav_repSecondLevel_1_repThirdLevel_2_repFourthLevel_1_fourthLevelLink_0"));

        // but if isDisplayed is false, then WebDriver may not be able to interact with the web element.
        Assert.assertFalse(jobAid.isDisplayed());

        try {
            jobAid.click();
        } catch (ElementNotInteractableException e) {
            // use Javascript instead
            ((JavascriptExecutor) driver).executeScript("arguments[0].click()", jobAid);
        }

        Assert.assertEquals(driver.getTitle(), "Job Aid Gallery | cn.ca");
    }

    @Test
    public void tc4() {
        WebElement search = driver.findElement(By.name("search"));

        actions.moveToElement(search).click().keyDown(Keys.SHIFT).sendKeys("abc").build().perform();

        // Expect capitalized characters in search field due to Shift key press
        Assert.assertEquals(search.getAttribute("value"), "ABC");
    }

    @AfterSuite
    public void tearDown() throws InterruptedException {
        driver.quit();
    }

}
