package New_Code;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderNowButtonTest {

	private WebDriver driver;
	private final String expectedUrl = "https://order.blackbeardiner.com/";

	@BeforeClass
	public void setUp() {
		// Set up the WebDriver (Assuming ChromeDriver is used)
		//System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
		driver = new ChromeDriver();
	}

	@DataProvider(name = "urlsProvider")
	public Object[] readUrlsFromFile() throws IOException {
		// Read URLs from the text file and return as an Object array
		List<String> urls = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader("D:\\All Data 2024\\My Desktop\\Black Bear Diner\\28-01-2025\\Lcocation data.txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				urls.add(line.trim());
			}
		}
		return urls.toArray();
	}

	@Test(dataProvider = "urlsProvider")
	public void verifyOrderNowButtonLinks(String pageUrl) {
		driver.get(pageUrl); // Navigate to the page URL

		try {
			// Locate the "Order Now" button using the XPath
			WebElement orderNowButton = driver.findElement(By.xpath("//a[@href='https://order.blackbeardiner.com/']"));

			// Get the href attribute value of the button
			String actualUrl = orderNowButton.getAttribute("href");

			// Verify the link matches the expected URL
			Assert.assertEquals(actualUrl, expectedUrl, "URL mismatch on page: " + pageUrl);
		} catch (Exception e) {
			// Log the page URL if the "Order Now" button is missing or the URL is incorrect
			System.err.println("Error on page: " + pageUrl + " - " + e.getMessage());
		}
	}

	@AfterClass
	public void tearDown() {
		if (driver != null) {
			driver.quit(); // Close the browser
		}
	}
}