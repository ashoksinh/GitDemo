package my_projects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.Assert;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SEO_Data_TESTNG {

	private WebDriver driver;
	private List<SEOData> seoDataList = new ArrayList<>();
	private static final long TIMEOUT_SECONDS = 10;

	static class SEOData {
		String url;
		String title;
		String description;
		String keywords;
		String canonicalLink;
		String mobileAlternateLink;
		Map<String, List<String>> headings;
		List<String> spellingErrors;
		int statusCode;
		double responseTime;
		String redirectUrl;
		String httpVersion;

		SEOData(String url, String title, String description, String keywords, String canonicalLink,
				String mobileAlternateLink, Map<String, List<String>> headings, List<String> spellingErrors,
				int statusCode, double responseTime, String redirectUrl, String httpVersion) {
			this.url = url;
			this.title = title;
			this.description = description;
			this.keywords = keywords;
			this.canonicalLink = canonicalLink;
			this.mobileAlternateLink = mobileAlternateLink;
			this.headings = headings;
			this.spellingErrors = spellingErrors;
			this.statusCode = statusCode;
			this.responseTime = responseTime;
			this.redirectUrl = redirectUrl;
			this.httpVersion = httpVersion;
		}
	}

	@BeforeTest
	public void setUp() {
		// Set up WebDriver using WebDriverManager
		WebDriverManager.chromedriver().setup();

		// Setup ChromeOptions to handle issues
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-extensions");
		options.addArguments("--disable-popup-blocking");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--remote-allow-origins=*");

		driver = new ChromeDriver(options);
	}

	@Test
	public void testSEODataExtraction() throws IOException {
		// Define the 4 specific URLs to analyze
		List<String> urlList = Arrays.asList("https://champagnemango.com/", "https://champagnemango.com/our-growing/",
				"https://champagnemango.com/recipe-gallery/", "https://champagnemango.com/where-to-buy-mangos/");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_SECONDS));

		// Loop through defined URLs and extract SEO metadata
		for (String pageUrl : urlList) {
			try {
				long startTime = System.nanoTime(); // Start time for response time

				driver.get(pageUrl); // Navigate to the page
				wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body"))); // Wait for page load

				long endTime = System.nanoTime(); // End time for response time

				double responseTimeInSeconds = (endTime - startTime) / 1_000_000_000.0; // Convert to seconds
				SEOData seoData = extractSEOData(driver, pageUrl, responseTimeInSeconds);
				seoDataList.add(seoData);
			} catch (Exception e) {
				System.err.println("Error processing URL: " + pageUrl);
				e.printStackTrace();
			}
		}

		// Validate that data is successfully collected
		Assert.assertEquals(seoDataList.size(), 4, "SEO data was not collected for all pages");
	}

	private SEOData extractSEOData(WebDriver driver, String url, double responseTime) throws IOException {
		String title = driver.getTitle();
		String keywords = "";
		String description = "";
		String canonicalLink = "";
		String mobileAlternateLink = "";
		Map<String, List<String>> headings = new HashMap<>();
		List<String> spellingErrors = new ArrayList<>();
		int statusCode = 0;
		String redirectUrl = null;
		String httpVersion = null;

		// Extract meta keywords, description, canonical link, and mobile alternate link
		// if present
		List<WebElement> metaElements = driver.findElements(By.tagName("meta"));
		for (WebElement metaElement : metaElements) {
			if ("keywords".equalsIgnoreCase(metaElement.getAttribute("name"))) {
				keywords = metaElement.getAttribute("content");
			}
			if ("description".equalsIgnoreCase(metaElement.getAttribute("name"))) {
				description = metaElement.getAttribute("content");
			}
		}

		List<WebElement> linkElements = driver.findElements(By.tagName("link"));
		for (WebElement linkElement : linkElements) {
			if ("canonical".equalsIgnoreCase(linkElement.getAttribute("rel"))) {
				canonicalLink = linkElement.getAttribute("href");
			}
			if ("alternate".equalsIgnoreCase(linkElement.getAttribute("rel"))
					&& "mobile".equalsIgnoreCase(linkElement.getAttribute("media"))) {
				mobileAlternateLink = linkElement.getAttribute("href");
			}
		}

		// Extract headings h1 to h6
		for (int i = 1; i <= 6; i++) {
			List<WebElement> headingElements = driver.findElements(By.tagName("h" + i));
			List<String> headingTexts = new ArrayList<>();
			for (WebElement headingElement : headingElements) {
				headingTexts.add(headingElement.getText());
			}
			headings.put("h" + i, headingTexts);
		}

		// Check for spelling errors (simple regex-based approach)
		List<WebElement> textElements = driver
				.findElements(By.xpath("//body//*[not(self::script) and not(self::style) and text()]"));
		Pattern pattern = Pattern.compile("\\b[A-Za-z]{2,}\\b");
		for (WebElement textElement : textElements) {
			Matcher matcher = pattern.matcher(textElement.getText());
			while (matcher.find()) {
				String word = matcher.group();
				if (!isCorrectlySpelled(word)) {
					spellingErrors.add(word);
				}
			}
		}

		// Check HTTP status, response time, redirect URL, and HTTP version
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setInstanceFollowRedirects(false); // No auto-redirect
		connection.connect();
		statusCode = connection.getResponseCode();
		httpVersion = connection.getHeaderField(null).split(" ")[0]; // Extract HTTP version
		if (statusCode >= 300 && statusCode < 400) {
			redirectUrl = connection.getHeaderField("Location");
		}

		return new SEOData(url, title, description, keywords, canonicalLink, mobileAlternateLink, headings,
				spellingErrors, statusCode, responseTime, redirectUrl, httpVersion);
	}

	private static boolean isCorrectlySpelled(String word) {
		// For now, this method will just return true as a placeholder.
		// If you have a dictionary or spell-check API, you can integrate it here.
		return word.matches("[A-Za-z]+");
	}

	@AfterTest
	public void tearDown() throws IOException {
		// Generate HTML dashboard
		try (FileWriter htmlWriter = new FileWriter("SEO_Dashboard.html")) {
			generateHTMLDashboard(seoDataList, htmlWriter, seoDataList.size());
		}
		// Close the browser
		if (driver != null) {
			driver.quit();
		}
	}

	private static void generateHTMLDashboard(List<SEOData> seoDataList, FileWriter writer, int totalPages)
	        throws IOException {
	    writer.write("<html><head><title>SEO Dashboard</title>");
	    
	    // Add Google Font link for Lexend
	    writer.write("<link href=\"https://fonts.googleapis.com/css2?family=Lexend&display=swap\" rel=\"stylesheet\">");
	    
	    // CSS Styles for the page with Lexend font family and centered titles
	    writer.write("<style>");
	    writer.write("body { background-color: #ffffff; color: #000000; font-family: 'Lexend', sans-serif; }"); // Set font family for the entire page
	    writer.write("table { width: 100%; border-collapse: collapse; font-family: 'Lexend', sans-serif; }"); // Apply font to table
	    writer.write("table, th, td { border: 1px solid #5F008A; }"); // Border color to match text color
	    writer.write("th, td { padding: 15px; text-align: left; color: #5F008A; font-family: 'Lexend', sans-serif; }"); // Set text color and font family for table headers and cells
	    writer.write("th { background-color: #FFFC9F; }"); // Set background color for table headers
	    writer.write("h1, h2, h3, h4, h5, h6 { color: #5F008A; font-family: 'Lexend', sans-serif; text-align: center; }"); // Set color, font family, and center text for heading elements
	    writer.write("p { text-align: center; }"); // Center align paragraphs
	    writer.write("</style></head><body>");
	    
	    // Center the SEO Dashboard title and page count
	    writer.write("<h1>SEO Dashboard</h1>");
	    writer.write("<p>Total pages checked: " + totalPages + "</p>");

	    writer.write(
	            "<table><tr><th>SR.No</th><th>URL</th><th>Title</th><th>Description</th><th>Keywords</th><th>Canonical Link</th><th>Mobile Alternate Link</th><th>Headings</th><th>Spelling Errors</th><th>Status Code</th><th>Response Time (s)</th><th>Redirect URL</th><th>HTTP Version</th></tr>");

	    int srNo = 1;
	    for (SEOData data : seoDataList) {
	        writer.write("<tr><td>" + srNo++ + "</td>");
	        writer.write("<td><a href=\"" + data.url + "\">" + data.url + "</a></td>");
	        writer.write("<td>" + data.title + "</td>");
	        writer.write("<td>" + data.description + "</td>");
	        writer.write("<td>" + data.keywords + "</td>");
	        writer.write("<td>" + data.canonicalLink + "</td>");
	        writer.write("<td>" + data.mobileAlternateLink + "</td>");
	        writer.write("<td>");
	        for (Map.Entry<String, List<String>> entry : data.headings.entrySet()) {
	            writer.write(
	                    "<strong>" + entry.getKey() + ":</strong> " + String.join(", ", entry.getValue()) + "<br>");
	        }
	        writer.write("</td>");
	        writer.write("<td>" + String.join(", ", data.spellingErrors) + "</td>");
	        writer.write("<td>" + data.statusCode + "</td>");
	        writer.write("<td>" + String.format("%.3f", data.responseTime) + "</td>");
	        writer.write("<td>" + (data.redirectUrl != null ? data.redirectUrl : "N/A") + "</td>");
	        writer.write("<td>" + data.httpVersion + "</td></tr>");
	    }

	    writer.write("</table></body></html>");
	}
}