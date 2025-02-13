package my_projects;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Video_test {

	public static void main(String[] args) throws InterruptedException {
		// Initialize WebDriver
		WebDriver driver = new ChromeDriver();

		// Map to hold the provided video data from Excel
		Map<String, String> providedVideoMap = new HashMap<>();

		// Read the existing Excel file
		try (FileInputStream fileIn = new FileInputStream(
				"D:\\All Data 2024\\My Desktop\\Oklahoma WIC\\WIC Video Filter\\provided_videos.xlsx")) {
			Workbook workbook = new XSSFWorkbook(fileIn);
			Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet

			for (Row row : sheet) {
				if (row.getRowNum() == 0) {
					continue; // Skip header row
				}
				Cell titleCell = row.getCell(0);
				Cell urlCell = row.getCell(1);

				if (titleCell != null && urlCell != null) {
					String title = titleCell.getStringCellValue();
					String url = urlCell.getStringCellValue();
					providedVideoMap.put(title, url);
				}
			}
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Navigate to the website
		driver.get("https://oklahoma.wicresources.org/videos/");

		// Wait for the page to load
		Thread.sleep(5000);

		// Dismiss any overlays or popups
		try {
			WebElement overlay = driver.findElement(By.id("onetrust-button-group-parent"));
			if (overlay.isDisplayed()) {
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", overlay);
			}
		} catch (NoSuchElementException e) {
			// Overlay not found; proceed normally
		}

		// Locate the category element
		WebElement categoryElement = driver
				.findElement(By.xpath("//li/a[@data-category='recipes' and contains(text(), 'Recipes (130)')]"));

		// Scroll the element into view and click using JavaScript
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", categoryElement);
		Thread.sleep(500); // Adding a small delay to ensure the element is scrolled into view
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", categoryElement);

		// Wait for the page to load
		Thread.sleep(5000);

		// Map to hold unique scraped video titles and URLs
		Map<String, String> scrapedVideoMap = new HashMap<>();
		Map<String, String> scrapedTitleMap = new HashMap<>(); // New map to store titles
		try {
			// Wait for elements to be present and "Load More" button to be clickable
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			boolean moreVideosAvailable = true;
			while (moreVideosAvailable) {
				// Extract video titles and URLs
				List<WebElement> videoElements = driver.findElements(By.cssSelector(".video-item"));
				for (WebElement videoElement : videoElements) {
					try {
						// Extract the title and URL from each video element
						WebElement titleElement = videoElement
								.findElement(By.xpath(".//div[@class='video-content']/h3"));
						WebElement linkElement = videoElement
								.findElement(By.xpath(".//div[@class='trigger video-thumbnail']"));
						String title = titleElement.getText();
						String url = linkElement.getAttribute("data-video-url");
						// Add to map to ensure uniqueness
						if (!scrapedVideoMap.containsKey(title)) {
							scrapedVideoMap.put(title, url);
							scrapedTitleMap.put(url, title); // Map URL to Title
						}
					} catch (Exception e) {
						System.out.println("Error extracting details from video element: " + e.getMessage());
					}
				}
				// Try to get the "Load More" button again after new content is loaded
				try {
					WebElement loadMoreButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("load-more")));
					if (loadMoreButton.isDisplayed() && loadMoreButton.isEnabled()) {
						loadMoreButton.click();
						// Wait for new content to load
						Thread.sleep(7000); // Increase the wait time
					} else {
						moreVideosAvailable = false; // Exit the loop if the button is not clickable
					}
				} catch (TimeoutException e) {
					// Handle the case when "Load More" button is no longer found or clickable
					moreVideosAvailable = false;
				}
			}

			// Create an HTML file for the comparison report
			try (PrintWriter writer = new PrintWriter(new FileWriter("video_comparison_report.html"))) {
				// Write the HTML header
				writer.println("<html>");
				writer.println("<head><title>Video Comparison Report</title></head>");
				writer.println("<body>");
				writer.println("<h1>Video Comparison Report</h1>");
				writer.println(
						"<table border='1' style='border-collapse: collapse; width: 100%; background-color: #f2f2f2;'>");
				writer.println("<tr style='background-color: #4CAF50; color: white;'>");
				writer.println("<th>S.No</th>");
				writer.println("<th>Title</th>");
				writer.println("<th>Provided URL</th>");
				writer.println("<th>Fetched Title</th>");
				writer.println("<th>Scraped URL</th>");
				writer.println("</tr>");

				// Compare the data and write to the HTML file
				int serialNo = 1;
				for (Map.Entry<String, String> entry : providedVideoMap.entrySet()) {
					String title = entry.getKey();
					String providedUrl = entry.getValue();
					String scrapedUrl = scrapedVideoMap.get(title);
					String fetchedTitle = scrapedTitleMap.getOrDefault(scrapedUrl, "Not Found"); // Fetch correct title

					boolean urlsMatch = normalizeUrl(providedUrl).equals(normalizeUrl(scrapedUrl));
					boolean titlesMatch = title.equals(fetchedTitle);

					String matchStatus = (urlsMatch && titlesMatch) ? "Match" : "Mismatch";
					String matchColor = matchStatus.equals("Match") ? "green" : "red";

					writer.println("<tr>");
					writer.println("<td>" + serialNo++ + "</td>");
					writer.println("<td style='color: " + (titlesMatch ? "black" : "red") + ";'>" + title + "</td>");
					writer.println(
							"<td style='color: " + (urlsMatch ? "black" : "red") + ";'>" + providedUrl + "</td>");
					writer.println("<td style='color: " + (fetchedTitle.equals("Not Found") ? "red" : "black") + ";'>"
							+ fetchedTitle + "</td>"); // Corrected title display
					writer.println("<td style='color: " + (scrapedUrl == null ? "red" : "black") + ";'>"
							+ (scrapedUrl == null ? "Not Found" : scrapedUrl) + "</td>");
					writer.println("</tr>");
				}

				// Write the HTML footer
				writer.println("</table>");
				writer.println("</body>");
				writer.println("</html>");

				System.out.println("Comparison report successfully written to HTML file.");

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Close the WebDriver
			driver.quit();
		}
	}

	// Helper method to normalize URLs for comparison
	private static String normalizeUrl(String url) {
		try {
			if (url == null || url.isEmpty()) {
				return "";
			}
			URL parsedUrl = new URL(url);
			return parsedUrl.getProtocol() + "://" + parsedUrl.getHost() + parsedUrl.getPath()
					+ (parsedUrl.getQuery() != null ? "?" + parsedUrl.getQuery() : "");
		} catch (Exception e) {
			return url;
		}
	}
}
