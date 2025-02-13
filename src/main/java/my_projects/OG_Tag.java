package my_projects;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class OG_Tag {

	public static void main(String[] args) throws IOException, InterruptedException {

		WebDriverManager.chromedriver().setup();

		WebDriver driver = new ChromeDriver();

		List<String> urlsWithoutOGTag = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(
				new FileReader("D:\\Ashoksinh Data\\My Desktop\\INK Agency\\DCA - 09-03-2023\\19-04-2024\\OG.txt"))) {
			String url;
			// Read each URL from the input text file
			while ((url = br.readLine()) != null) {
				driver.get(url);
				// Check if the OG tag exists
				if (!isOGTagPresent(driver)) {
					urlsWithoutOGTag.add(url);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Close the WebDriver

			// driver.quit();
		}

		// Write URLs without OG tag to a text file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("urls_without_og.txt"))) {
			for (String url : urlsWithoutOGTag) {
				writer.write(url);
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean isOGTagPresent(WebDriver driver) {
		// Check if the OG tag exists
		try {
			WebElement ogTag = driver.findElement(By.xpath("//meta[@property='og:title']"));
			return ogTag != null;
		} catch (Exception e) {
			return false;
		}
	}
}