package ca.testautomation.actionstest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class ActionsTest {
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

    @Test
    public void tc1() {
        driver.manage().window().maximize();
        driver.get("https://cn.ca/en/");

        // don't use id as a locator, as this site implements dynamic id
        WebElement service = driver.findElement(By.cssSelector("div>a[href='/en/our-services/'][class='topLevelLink']"));
        WebElement trucking = driver.findElement(By.cssSelector("li>a[href='/en/our-services/trucking/'][class='secondlevellink']"));
        WebElement express = driver.findElement(By.cssSelector("li>a[href='/en/our-services/trucking/cn-express-pass/'][class='thirdlevellink']"));
        WebElement jobAid = driver.findElement(By.cssSelector("li>a[href='/en/our-services/trucking/cn-express-pass/job-aid-gallery/'][class='fourthlevellink']"));

        // increase pause duration if getting element not interactable exception on mouseover
        actions.moveToElement(service).pause(2000)
                .moveToElement(trucking).pause(1000)
                .moveToElement(express).pause(1000)
                .moveToElement(jobAid).pause(1000)
                .click().build().perform();

        Assert.assertEquals(driver.getTitle(), "Job Aid Gallery | cn.ca");
    }

    @Test
    public void tc2() {
        driver.manage().window().maximize();
        driver.get("https://cn.ca/en/");

        WebElement service = driver.findElement(By.cssSelector("div>a[href='/en/our-services/'][class='topLevelLink']"));

        // Opens the service page on a new tab
        // .pause(3000) is an inline alternative to a separate line of Thread.sleep(3000)
        actions.moveToElement(service).keyDown(Keys.CONTROL).click().pause(3000).build().perform();

        Assert.assertEquals(driver.getWindowHandles().size(), 2);
    }

    @Test
    public void tc3() {
        driver.manage().window().maximize();
        driver.get("https://cn.ca/en/");

        // web element exists in DOM
        WebElement jobAid = driver.findElement(By.cssSelector("li>a[href='/en/our-services/trucking/cn-express-pass/job-aid-gallery/'][class='fourthlevellink']"));

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
        driver.manage().window().maximize();
        driver.get("https://cn.ca/en/");

        WebElement search = driver.findElement(By.name("search"));

        actions.moveToElement(search).click().keyDown(Keys.SHIFT).sendKeys("abc").build().perform();

        // expect capitalized characters in search field due to Shift key press
        Assert.assertEquals(search.getAttribute("value"), "ABC");
    }

    @Test
    public void tc5() throws InterruptedException {
        driver.manage().window().maximize();
        driver.get("https://jqueryui.com/droppable/");

        // remove this line when not visually verifying the steps
        Thread.sleep(1500);

        WebElement frm = driver.findElement(By.className("demo-frame"));
        driver.switchTo().frame(frm);

        WebElement draggable = driver.findElement(By.id("draggable"));
        WebElement droppable = driver.findElement(By.id("droppable"));

        String drpText = driver.findElement(By.xpath("//*[@id='droppable']/p")).getText();
        Assert.assertTrue(drpText.equals("Drop here"));

        actions.dragAndDrop(draggable, droppable).build().perform();

        drpText = driver.findElement(By.xpath("//*[@id='droppable']/p")).getText();
        Assert.assertTrue(drpText.equals("Dropped!"));
    }

    @Test
    public void tc6() throws InterruptedException {
        driver.manage().window().maximize();
        driver.get("https://developer.mozilla.org/en-US/docs/Web/API/Element/dblclick_event");

        WebElement frameExample = driver.findElement(By.id("frame_examples"));
        driver.switchTo().frame(frameExample);

        // there is only one "aside" tag in this frame
        WebElement card = driver.findElement(By.tagName("aside"));

        // Resolves the "move target out of bounds" exception
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false)", card);
        Thread.sleep(1000);

        Assert.assertEquals(card.getAttribute("class"), "");

        actions.doubleClick(card).build().perform();

        // confirm that card size is larger after double-click
        Assert.assertEquals(card.getAttribute("class"), "large");
    }

    @Test
    public void tc7() throws InterruptedException {
        driver.manage().window().maximize();
        driver.get("https://swisnl.github.io/jQuery-contextMenu/demo.html");

        WebElement contextMenu = driver.findElement(By.cssSelector("div[class='document']>p>span[class='context-menu-one btn btn-neutral']"));
        actions.contextClick(contextMenu).build().perform();

        // Quit
        driver.findElement(By.cssSelector("ul[class='context-menu-list context-menu-root'] > li[class='context-menu-item context-menu-icon context-menu-icon-quit'] > span")).click();

        // Get alert text
        String alertText = driver.switchTo().alert().getText();

        // remove this line when not visually verifying the steps
        Thread.sleep(3000);

        // Click on "OK" button in alert.
        driver.switchTo().alert().accept();

        Assert.assertTrue(alertText.equals("clicked: quit"));
    }

    @AfterSuite
    public void tearDown() throws InterruptedException {
        // remove this line when not visually verifying the steps
        Thread.sleep(3000);

        driver.quit();
    }

}
