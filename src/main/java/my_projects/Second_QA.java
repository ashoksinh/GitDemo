package my_projects;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Second_QA {
	@Test(priority = 1)
	public void MultiplepagesH1() throws FileNotFoundException, IOException, InterruptedException {

		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();

		// driver.get("https://marketplacemil.wpengine.com/");

		String filePath = "D:\\Ashoksinh Data\\My Desktop\\Align Pregnancy Services\\Alt Tag.txt";

		// List to keep track of URLs with no H1 tags
		List<String> urlsWithoutH1 = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String url;
			while ((url = reader.readLine()) != null) {
				driver.get(url);

				// Find all H1 tags on the page
				List<WebElement> h1Tags = driver.findElements(By.tagName("h1"));

				// Check if there are H1 tags on the page
				if (h1Tags.isEmpty()) {
					// If no H1 tags found, add to list
					urlsWithoutH1.add(url);
					System.out.println("No H1 tags found on the page: " + url);
				}

				else {
					// Print the text of each H1 tag
					for (WebElement h1Tag : h1Tags) {
						System.out.println("H1 Tag is:\t" + h1Tag.getText());
					}
				}

				// Optional delay for page transitions
				Thread.sleep(2000);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		} finally {
			// Close WebDriver
			if (driver != null) {
				driver.close();
			}
		}

		// Output URLs with no H1 tags
		if (!urlsWithoutH1.isEmpty()) {
			System.out.println("Pages without H1 tags:");
			for (String urlWithoutH1 : urlsWithoutH1) {
				System.out.println(urlWithoutH1);
			}
		} else {
			System.out.println("All pages contained H1 tags.");
		}
	}
}
