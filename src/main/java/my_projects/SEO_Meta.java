package my_projects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SEO_Meta {

	public static void main(String[] args) {

		// Set Chrome options
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");

		WebDriver driver = new ChromeDriver(options);

		// Define file paths
		String liveUrlsFile = "D:\\All Data 2024\\My Desktop\\Black Bear Diner\\Automation File\\Live URLS.txt";
		String stagingUrlsFile = "D:\\All Data 2024\\My Desktop\\Black Bear Diner\\Automation File\\Stage.txt";

		// Check if files exist before proceeding
		File liveFile = new File(liveUrlsFile);
		File stageFile = new File(stagingUrlsFile);

		if (!liveFile.exists()) {
			System.out.println("File not found: " + liveUrlsFile);
			driver.quit();
			return;
		}

		if (!stageFile.exists()) {
			System.out.println("File not found: " + stagingUrlsFile);
			driver.quit();
			return;
		}

		// Read URLs from both live and stage files
		List<String> liveUrls = readUrlsFromFile(liveUrlsFile);
		List<String> stageUrls = readUrlsFromFile(stagingUrlsFile);

		// Ensure the sizes of live and stage URL lists match
		int size = Math.min(liveUrls.size(), stageUrls.size());

		// Generate the report
		generateReport(driver, liveUrls, stageUrls, size);

		// Quit the driver
		driver.quit();
	}

	private static List<String> readUrlsFromFile(String filePath) {
		List<String> urlList = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String url = line.trim();
				if (!url.isEmpty()) {
					urlList.add(url);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return urlList;
	}

	private static void generateReport(WebDriver driver, List<String> liveUrls, List<String> stageUrls, int size) {
		try (FileWriter writer = new FileWriter("SEO_Meta_Report.html")) {
			writer.write("<html><head><style>");
			writer.write("body {font-family: Arial, sans-serif; margin: 0; padding: 0;}");
			writer.write(".background-container {");
			writer.write("position: fixed;");
			writer.write("top: 0; left: 0; width: 100%; height: 100%;");
			writer.write("background-image: url('D:\\\\All Data 2024\\\\My Desktop\\\\Black Bear Diner\\\\Automation File\\\\GOT.jpg');");
			writer.write("background-size: cover;");
			writer.write("background-position: center;");
			writer.write("background-repeat: no-repeat;");
			writer.write("filter: blur(10px);");
			writer.write("z-index: -1;");
			writer.write("}");
			writer.write(".content {position: relative; z-index: 1; padding: 20px;}");
			writer.write("table {width: 100%; border-collapse: collapse; margin-top: 20px;");
			writer.write("background-color: rgba(255, 255, 255, 0.8);"); // Add transparency to table background
			writer.write("}");
			writer.write("th, td {border: 1px solid black; padding: 10px; text-align: left;}");
			writer.write("th {background-color: #4CAF50; color: white;}");
			writer.write("td.matched {background-color: #d4edda; color: #155724; font-weight: bold;}");
			writer.write("td.unmatched {background-color: #f8d7da; color: #721c24; font-weight: bold;}");
			writer.write("td.default {background-color: #fff3cd; color: #856404; font-weight: bold;}");
			writer.write("td.highlight {background-color: yellow;}");
			writer.write("</style></head><body>");
			writer.write("<div class='background-container'></div>");
			writer.write("<div class='content'>");
			writer.write("<h2>SEO Meta Title and Description Comparison</h2>");
			writer.write("<table>");
			writer.write(
					"<tr><th>Sr No.</th><th>Live URL</th><th>Meta Title (Live)</th><th>Meta Description (Live)</th>");
			writer.write("<th>Stage URL</th><th>Meta Title (Stage)</th><th>Meta Description (Stage)</th>");
			writer.write("<th>Matched Meta Title</th><th>Matched Meta Description</th></tr>");
			int srNo = 1;

			for (int i = 0; i < size; i++) {
				String liveUrl = liveUrls.get(i);
				String stageUrl = stageUrls.get(i);

				System.out.println("Processing Live URL: " + liveUrl + " and Stage URL: " + stageUrl);

				// Process live and stage URL once, side by side
				String[] liveMeta = getMetaTags(driver, liveUrl);
				String[] stageMeta = getMetaTags(driver, stageUrl);

				boolean isTitleMatch = liveMeta[0].equals(stageMeta[0]);
				boolean isDescriptionMatch = liveMeta[1].equals(stageMeta[1]);

				String titleMatchSymbol = isTitleMatch ? "✓" : "X";
				String descriptionMatchSymbol = isDescriptionMatch ? "✓" : "X";

				boolean isHighlight = !isTitleMatch || !isDescriptionMatch;

				String titleClass = isTitleMatch ? "matched" : "unmatched";
				String descriptionClass = isDescriptionMatch ? "matched" : "unmatched";

				writer.write("<tr>");
				writer.write("<td>" + srNo++ + "</td>");
				writer.write("<td>" + liveUrl + "</td>");
				writer.write("<td class='" + titleClass + "'>" + liveMeta[0] + "</td>");
				writer.write("<td class='" + descriptionClass + "'>" + liveMeta[1] + "</td>");
				writer.write("<td>" + stageUrl + "</td>");
				writer.write("<td class='" + titleClass + "'>" + stageMeta[0] + "</td>");
				writer.write("<td class='" + descriptionClass + "'>" + stageMeta[1] + "</td>");
				writer.write("<td>" + titleMatchSymbol + "</td>");
				writer.write("<td>" + descriptionMatchSymbol + "</td>");
				writer.write("</tr>");
			}

			writer.write("</table></body></html>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String[] getMetaTags(WebDriver driver, String url) {
		try {
			driver.get(url);
			Thread.sleep(2000); // Replace with WebDriverWait if needed

			String title = driver.getTitle();
			String description;

			try {
				description = driver.findElement(By.cssSelector("meta[name='description']")).getAttribute("content");
			} catch (Exception e) {
				description = "No description found";
			}

			return new String[] { title.isEmpty() ? "No title found" : title,
					description.isEmpty() ? "No description found" : description };
		} catch (Exception e) {
			System.out.println("Error fetching meta tags for URL: " + url + " - " + e.getMessage());
			return new String[] { "No title found", "No description found" };
		}
	}
}
