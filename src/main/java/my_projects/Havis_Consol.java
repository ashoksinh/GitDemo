package my_projects;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Havis_Consol {

	public static void main(String[] args)throws InterruptedException {
		// TODO Auto-generated method stub

		WebDriverManager.chromedriver().setup();

		WebDriver driver = new ChromeDriver();
		// Path to your file with URLs
		driver.manage().window().maximize();

		driver.get("https://www.havis.com/console-configurator/");

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Make Dropdown
        WebElement makeDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("make")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", makeDropdown);
        makeDropdown.click();

        // Wait for the Chevrolet option to be clickable and click it
        WebElement chevroletOption = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Chevrolet")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", chevroletOption);
       
        int attempts = 0;
        while (attempts < 3) {
            try {
                chevroletOption.click();
                break;
            } catch (Exception e) {
                attempts++;
                Thread.sleep(1000); // Wait for 1 second before retrying
            }
        }
        
        //chevroletOption.click();
     //   wait.until(ExpectedConditions.invisibilityOf(chevroletOption)); // Wait for the option to be invisible

        // Model Dropdown
        WebElement modelDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("model")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", modelDropdown);
        modelDropdown.click();

        // Tahoe Option
        WebElement modelValues = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Tahoe")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", modelValues);
        
        int attemptsmodel= 0;
        while (attemptsmodel < 3) {
            try {
            	modelValues.click();
                break;
            } catch (Exception e) {
                attempts++;
                Thread.sleep(1000); // Wait for 1 second before retrying
            }
        }
        
        //modelValues.click();
       // wait.until(ExpectedConditions.invisibilityOf(modelValues)); // Wait for the option to be invisible

        // Year Dropdown
        WebElement yearDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("year")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", yearDropdown);
        yearDropdown.click();

        // 2024 Option
        WebElement yearValues = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("2024")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", yearValues);
        int attemptsYear= 0;
        while (attemptsYear < 3) {
            try {
            	yearValues.click();
                break;
            } catch (Exception e) {
                attempts++;
                Thread.sleep(1000); // Wait for 1 second before retrying
            }
        }
        
 //       yearValues.click();
        wait.until(ExpectedConditions.invisibilityOf(yearValues)); // Wait for the option to be invisible

        Thread.sleep(5000);
        
        // Submit Button
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html[1]/body[1]/div[2]/div[1]/section[3]/div[1]/div[1]/div[2]/form[1]/div[4]")));
       // ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitButton);
        submitButton.click();

        
		Thread.sleep(5000);

		WebElement newElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@id='page']/div[1]/section[4]/div[2]/div[1]/div[2]/div[1]/div[2]/ul[1]/li[1]/div[1]")));

		driver.findElement(By.xpath("//body/div[@id='page']/div[1]/section[4]/div[2]/div[1]/div[2]/div[1]/div[2]/ul[1]/li[1]/div[1]")).click();

		//driver.quit();
		
		WebElement nextButton1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//body/div[@id='page']/div[1]/section[4]/div[2]/div[2]/div[1]/div[1]/a[1]")));
		nextButton1.click();
				
		Thread.sleep(5000);
		
		driver.findElement(By.xpath("//body/div[@id='page']/div[1]/div[2]/div[2]/div[2]/div[2]/div[2]/a[1]")).click();
		
		//Equipment Brands
		driver.findElement(By.xpath("//body/div[@id='page']/div[1]/div[2]/div[1]/div[1]/div[1]/div[4]/div[2]/ul[1]/li[7]/div[1]")).click();
		
		
		
		
		
	}

}
