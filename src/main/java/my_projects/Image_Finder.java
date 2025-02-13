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

public class Image_Finder {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		WebDriverManager.chromedriver().setup();

		WebDriver driver = new ChromeDriver();

		String imagePath = "https://d3mhhyy97dj42z.cloudfront.net/wp-content/uploads/2022/05/hm-blog-make-it.jpg";

		// Read URLs from text file
		List<String> urls = readURLsFromFile("D:\\Ashoksinh Data\\My Desktop\\Havis\\Havis URLs 13-05-2024\\URLS.txt");

		// List to store URLs with image present
		List<String> urlsWithImage = new ArrayList<>();

		// Iterate through each URL
		for (String url : urls) {
			// Navigate to URL
			driver.get(url);

			// Check if image with specified path exists
			if (isImagePresent(driver, imagePath)) {
				urlsWithImage.add(url);
			}
		}

		// Write URLs with image present to text file
		writeURLsToFile(urlsWithImage, "urls_with_image.txt");

		// Quit WebDriver
		// Quit WebDriver
		driver.quit();
	}

	// Method to read URLs from text file
	private static List<String> readURLsFromFile(String filePath) {
		List<String> urls = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				urls.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return urls;
	}

	// Method to check if image is present on the page
	private static boolean isImagePresent(WebDriver driver, String imagePath) {
		List<WebElement> images = driver.findElements(By.xpath(
				"/html/body/div[2]/div/div/section[7]/div/div[4]/div/div/div/div/div[1]/div[2]" + imagePath + "']"));
		return !images.isEmpty();
	}

	// Method to write URLs to text file
	private static void writeURLsToFile(List<String> urls, String filePath) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
			for (String url : urls) {
				bw.write(url);
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
