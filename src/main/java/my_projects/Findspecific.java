package my_projects;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Findspecific {

	@Test
	public void specific() {

		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		;
		driver.manage().window().maximize();
		// List of URLs representing multiple pages
		String[] pageUrls = { "https://marketplacemil:markmil023@marketplacemil.wpengine.com",
				"https://marketplacemil.wpengine.com/product-category/splice-one/" };

		// Text to search for
		String searchText = "Featured PRODUCTS";

		// Iterate through each page
		for (String url : pageUrls) {
			// Navigate to the page
			driver.get(url);

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			WebElement element = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//*[contains(text(), '" + searchText + "')]")));

			// Check if the text is found
			if (element.isDisplayed()) {
				System.out.println("Text found on page: " + url);
			} else {
				System.out.println("Text not found on page: " + url);
			}
		}

		// Close the browser
		driver.quit();
	}
}
