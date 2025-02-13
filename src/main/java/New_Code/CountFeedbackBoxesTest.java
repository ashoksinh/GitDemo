package New_Code;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CountFeedbackBoxesTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        driver.get("https://www.offerpad.com/reviews/"); }

    @Test
    public void countFeedbackBoxes() throws InterruptedException {
        // Locators
        By viewMoreButton = By.cssSelector("a.loadmore_button");
        By feedbackBoxes = By.xpath("//div[contains(@class, 'feedback')]");

        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Step 1: Scroll to the "View More" button after page load
        while (true) {
            try {
                WebElement button = driver.findElement(viewMoreButton);
                js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", button);
                Thread.sleep(500);
                if (button.isDisplayed()) {
                    break;
                }
            } catch (Exception e) {
                js.executeScript("window.scrollBy(0, 500);");
                Thread.sleep(500);
            }
        }

        // Step 2: Click the "View More" button, wait for results, and scroll again
        while (true) {
            try {
                WebElement button = wait.until(ExpectedConditions.elementToBeClickable(viewMoreButton));

                // Scroll to ensure the button is visible
                js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", button);
                Thread.sleep(500);

                // Click the button
                button.click();

                // Wait for new content to load
                Thread.sleep(2000);

                // Scroll again to check the button's position
                js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", button);
            } catch (Exception e) {
                // Break the loop if the button is no longer present or clickable
                break;
            }
        }

        // Step 3: Count the number of feedback boxes
        List<WebElement> boxes = driver.findElements(feedbackBoxes);
        int boxCount = boxes.size();

        System.out.println("Total number of feedback boxes: " + boxCount);

        Assert.assertTrue(boxCount > 0, "Box count should be greater than zero");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
