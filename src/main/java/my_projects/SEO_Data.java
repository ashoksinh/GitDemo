package my_projects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SEO_Data {

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

    public static void main(String[] args) {
        // Set up WebDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();
        
        // Setup ChromeOptions to handle issues
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        // Uncomment if you want to run headless
        // options.addArguments("--headless");

        WebDriver driver = new ChromeDriver(options);

        // Set an implicit wait
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        try (FileWriter htmlWriter = new FileWriter("SEO_Dashboard.html")) {

            // Define the 4 specific URLs to analyze
            List<String> urlList = Arrays.asList(
                "https://champagnemango.com/",
                "https://champagnemango.com/our-growing/",
                "https://champagnemango.com/recipe-gallery/",
                "https://champagnemango.com/where-to-buy-mangos/"
            );

            // List to hold SEO data for HTML generation
            List<SEOData> seoDataList = new ArrayList<>();

            // Loop through defined URLs and extract SEO metadata
            for (String pageUrl : urlList) {
                try {
                    long startTime = System.currentTimeMillis(); // Start time for response time
                    SEOData seoData = extractSEOData(driver, pageUrl);
                    long endTime = System.currentTimeMillis(); // End time for response time
                    seoData.responseTime = (endTime - startTime) / 1000.0; // Calculating response time in seconds with decimals
                    seoDataList.add(seoData);
                } catch (Exception e) {
                    System.err.println("Error processing URL: " + pageUrl);
                    e.printStackTrace();
                }
            }

            // Generate HTML dashboard
            generateHTMLDashboard(seoDataList, htmlWriter, urlList.size());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();
        }
    }

    private static SEOData extractSEOData(WebDriver driver, String url) throws IOException {
        driver.get(url);
        
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

        // Extract meta keywords, description, canonical link, and mobile alternate link if present
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
                spellingErrors, statusCode, 0, redirectUrl, httpVersion);
    }

    private static boolean isCorrectlySpelled(String word) {
        return word.matches("[A-Za-z]+");
    }

    private static void generateHTMLDashboard(List<SEOData> seoDataList, FileWriter writer, int totalPages)
            throws IOException {
        writer.write("<html><head><title>SEO Dashboard</title>");
        writer.write("<style>table { width: 100%; border-collapse: collapse; }");
        writer.write("table, th, td { border: 1px solid black; }");
        writer.write("th, td { padding: 15px; text-align: left; }</style></head><body>");
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
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 /*
		 WebDriverManager.chromedriver().setup();
	       
	        ChromeOptions options = new ChromeOptions();
	        WebDriver driver = new ChromeDriver(options);

	        // Set an implicit wait
	        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

	        try (FileWriter seoWriter = new FileWriter("SEO_Data.txt");
	             FileWriter urlWriter = new FileWriter("SEO-All_Pages_Urls.txt")) {

	            // Start at the homepage
	            String homepageUrl = "https://champagnemango.com/";
	            driver.get(homepageUrl);

	            // Extract all links from the homepage
	            Set<String> allPageUrls = extractAllLinks(driver, homepageUrl);

	            // Write the number of pages to seo_data.txt
	            seoWriter.write("Total pages checked: " + allPageUrls.size() + "\n");

	            // Loop through all collected URLs and extract SEO metadata
	            for (String pageUrl : allPageUrls) {
	                try {
	                    driver.get(pageUrl);
	                    extractAndPrintSEOData(driver, pageUrl, seoWriter);
	                    urlWriter.write(pageUrl + "\n");
	                } catch (Exception e) {
	                    System.err.println("Error processing URL: " + pageUrl);
	                    e.printStackTrace();
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            // Close the browser
	            driver.quit();
	        }
	    }

	    private static void extractAndPrintSEOData(WebDriver driver, String url, FileWriter writer) throws IOException {
	        String title = driver.getTitle();
	        String keywords = "";
	        String description = "";

	        // Extract meta keywords and description if present
	        List<WebElement> metaElements = driver.findElements(By.tagName("meta"));
	        for (WebElement metaElement : metaElements) {
	            if ("keywords".equalsIgnoreCase(metaElement.getAttribute("name"))) {
	                keywords = metaElement.getAttribute("content");
	            }
	            if ("description".equalsIgnoreCase(metaElement.getAttribute("name"))) {
	                description = metaElement.getAttribute("content");
	            }
	        }

	        // Write the extracted SEO metadata to the file
	        writer.write("URL: " + url + "\n");
	        writer.write("Title: " + title + "\n");
	        writer.write("Description: " + description + "\n");
	        writer.write("Keywords: " + keywords + "\n");
	        writer.write("----------------------------------------\n");
	    }

	    private static Set<String> extractAllLinks(WebDriver driver, String baseUrl) {
	        Set<String> links = new HashSet<>();
	        Set<String> visited = new HashSet<>();
	        links.add(baseUrl);

	        while (!links.isEmpty()) {
	            String currentUrl = links.iterator().next();
	            links.remove(currentUrl);
	            if (!visited.contains(currentUrl)) {
	                try {
	                    driver.get(currentUrl);
	                    visited.add(currentUrl);

	                    // Use explicit wait to ensure the page is loaded
	                    new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

	                    // Find all anchor elements
	                    List<WebElement> anchorElements = driver.findElements(By.tagName("a"));
	                    for (WebElement anchorElement : anchorElements) {
	                        String href = null;
	                        try {
	                            href = anchorElement.getAttribute("href");
	                            if (href != null && !href.isEmpty() && !href.equals("#") && !href.endsWith("#")) {
	                                URL url = new URL(href);
	                                if (url.getHost().equals(new URL(baseUrl).getHost())) {
	                                    links.add(href);
	                                }
	                            }
	                        } catch (MalformedURLException e) {
	                            // Convert relative URL to absolute URL
	                            try {
	                                URL absoluteUrl = new URL(new URL(currentUrl), href);
	                                if (absoluteUrl.getHost().equals(new URL(baseUrl).getHost())) {
	                                    links.add(absoluteUrl.toString());
	                                }
	                            } catch (MalformedURLException ex) {
	                                ex.printStackTrace();
	                            }
	                        } catch (StaleElementReferenceException e) {
	                            // Retry fetching the anchor element
	                            anchorElement = driver.findElement(By.xpath("//a[@href='" + href + "']"));
	                        }
	                    }
	                } catch (Exception e) {
	                    System.err.println("Error processing URL: " + currentUrl);
	                    e.printStackTrace();
	                }
	            }
	        }

	        return visited;
	    }
	}

*/