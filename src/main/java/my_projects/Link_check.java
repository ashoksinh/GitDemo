package my_projects;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Link_check {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		// Path to your ChromeDriver executable
		WebDriverManager.chromedriver().setup();

		WebDriver driver = new ChromeDriver();

		// String filePath = "D:\\Ashoksinh Data\\My Desktop\\DynaFire\\Dyna.txt";

		// Read URLs from a text file (replace with your file path)
		String filePath = "D:\\Ashoksinh Data\\My Desktop\\DynaFire\\Dyna.txt";
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String url;

		while ((url = reader.readLine()) != null) {
			// Visit each URL
			driver.get(url);

			// Find all links on the page
			List<WebElement> links = driver.findElements(By.tagName("a"));

			// Iterate through each link and check if it opens in a new tab
			for (WebElement link : links)

			{

				String target = link.getAttribute("target");
				if (target != null && target.equals("_blank")) {
					// Print the text associated with the link
					System.out.println("Link Text: " + link.getText());
				}
			}
		}

		// Close the browser
		// driver.quit();
	}
}
