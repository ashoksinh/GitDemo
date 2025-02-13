package my_projects;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Ptag_Checker {
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();

		List<String> incorrectPages = new ArrayList<>();
		// Path to the file containing page URLs
		String filePath = "D:\\Ashoksinh Data\\My Desktop\\ADVault\\All Pages.txt";

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String pageUrl;
			// Read each line from the file
			while ((pageUrl = br.readLine()) != null) {
				// Check on Desktop resolution
				checkFontSize(driver, pageUrl, 1920, 19, incorrectPages);

				// Check on iPad resolution
				driver.manage().window().setSize(new Dimension(1024, 768)); // Adjust as per iPad resolution
				checkFontSize(driver, pageUrl, 1024, 17, incorrectPages);

				// Check on Mobile resolution
				driver.manage().window().setSize(new Dimension(440, 768)); // Adjust as per Mobile resolution
				checkFontSize(driver, pageUrl, 440, 15, incorrectPages);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Close the browser
			// driver.quit();
		}

		// Print the list of pages with incorrect font sizes
		if (incorrectPages.isEmpty()) {
			System.out.println("All pages have correct font sizes.");
		} else {
			System.out.println("Pages with incorrect font sizes:");
			for (String page : incorrectPages) {
				System.out.println(page);
			}
		}
	}

	// Method to check font size for a given page URL and resolution
	private static void checkFontSize(WebDriver driver, String pageUrl, int screenWidth, int expectedFontSize,
			List<String> incorrectPages) {
		driver.manage().window().setSize(new Dimension(screenWidth, 768)); // Set window size for the resolution
		driver.get(pageUrl);

		// Find all <p> tags on the page
		List<WebElement> pTags = driver.findElements(By.tagName("p"));

		// Iterate through <p> tags
		for (WebElement pTag : pTags) {
			// Get the font size of the <p> tag
			String fontSize = pTag.getCssValue("font-size");

			// Check if the font size matches the expected value
			if (!fontSize.equals(expectedFontSize + "px")) {
				// Add the page URL to the list of incorrect pages
				incorrectPages.add(pageUrl);
				// Break the loop if one <p> tag has incorrect font size
				break;
			}
		}
	}
}











/*
WebDriverManager.firefoxdriver().setup();
 WebDriver driver = new FirefoxDriver();


 List<String> incorrectPages = new ArrayList<>();

 // Path to the file containing page URLs
 String filePath = "D:\\Ashoksinh Data\\My Desktop\\ADVault\\All Pages.txt";

 try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
     String pageUrl;
     // Read each line from the file
     while ((pageUrl = br.readLine()) != null) {
         // Open the page
         driver.get(pageUrl);

         // Find all <p> tags on the page
         List<WebElement> pTags = driver.findElements(By.tagName("p"));

         // Iterate through <p> tags
         for (WebElement pTag : pTags) {
             // Get the font size of the <p> tag
             String fontSize = pTag.getCssValue("font-size");

             // Check if the font size is not 20px
             if (!fontSize.equals("19px")) {
                 // Add the page URL to the list of incorrect pages
                 incorrectPages.add(pageUrl);
                 // Break the loop if one <p> tag has incorrect font size
                 break;
             }
         }
     }
 } catch (IOException e) {
     e.printStackTrace();
 }

 // Print the list of pages with incorrect font sizes
 if (incorrectPages.isEmpty()) {
     System.out.println("All pages have correct font sizes.");
 } else {
     System.out.println("Pages with incorrect font sizes:");
     for (String page : incorrectPages) {
         System.out.println(page);
     }
 }

 // Close the browser
//  driver.quit();
}
}

*/



