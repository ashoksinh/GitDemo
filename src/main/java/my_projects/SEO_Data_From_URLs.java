package my_projects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SEO_Data_From_URLs {

    static class SEOData {
        String url;
        String title;
        String description;
        String keywords;
        String canonicalLink;
        String mobileAlternateLink;
        String httpsStatus;
        Map<String, List<String>> headings;
        List<String> spellingErrors;

        SEOData(String url, String title, String description, String keywords, String canonicalLink, String mobileAlternateLink, String httpsStatus, Map<String, List<String>> headings, List<String> spellingErrors) {
            this.url = url;
            this.title = title;
            this.description = description;
            this.keywords = keywords;
            this.canonicalLink = canonicalLink;
            this.mobileAlternateLink = mobileAlternateLink;
            this.httpsStatus = httpsStatus;
            this.headings = headings;
            this.spellingErrors = spellingErrors;
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(SEO_Data_From_URLs.class);

    public static void main(String[] args) {
        // Set up WebDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        WebDriver driver = new ChromeDriver(options);

        // Set an implicit wait
      //  driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        try (FileWriter htmlWriter = new FileWriter("SEO_Dashboard_From_urls.html")) {

            // Read URLs from the text file
            List<String> allPageUrls = readUrlsFromFile("D:\\All Data 2024\\My Desktop\\Black Bear Diner\\Alt tag - Copy.txt");

            // List to hold SEO data for HTML generation
            List<SEOData> seoDataList = new ArrayList<>();

            // Loop through all collected URLs and extract SEO metadata
            for (String pageUrl : allPageUrls) {
                try {
                    driver.get(pageUrl);
                    SEOData seoData = extractSEOData(driver, pageUrl);
                    seoDataList.add(seoData);
                } catch (Exception e) {
                    logger.error("Error processing URL: " + pageUrl, e);
                }
            }

            // Generate HTML dashboard
            generateHTMLDashboard(seoDataList, htmlWriter, allPageUrls.size());

        } catch (IOException e) {
            logger.error("Error writing to file", e);
        } finally {
            // Close the browser
            driver.close();
        }
    }

    private static List<String> readUrlsFromFile(String fileName) throws IOException {
        List<String> urls = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                urls.add(line.trim());
            }
        }
        return urls;
    }

    private static SEOData extractSEOData(WebDriver driver, String url) {
        String title = driver.getTitle();
        String keywords = "";
        String description = "";
        String canonicalLink = "";
        String mobileAlternateLink = "";
        String httpsStatus = url.startsWith("https") ? "Yes" : "No";
        Map<String, List<String>> headings = new HashMap<>();
        List<String> spellingErrors = new ArrayList<>();

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
            if ("alternate".equalsIgnoreCase(linkElement.getAttribute("rel")) && "mobile".equalsIgnoreCase(linkElement.getAttribute("media"))) {
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
        List<WebElement> textElements = driver.findElements(By.xpath("//body//*[not(self::script) and not(self::style) and text()]"));
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

        return new SEOData(url, title, description, keywords, canonicalLink, mobileAlternateLink, httpsStatus, headings, spellingErrors);
    }

    private static boolean isCorrectlySpelled(String word) {
        // Placeholder for actual spell-check logic
        // You can integrate with an API or use a dictionary for more accurate spell-checking
        return word.matches("[A-Za-z]+");
    }

    private static void generateHTMLDashboard(List<SEOData> seoDataList, FileWriter writer, int totalPages) throws IOException {
        writer.write("<html><head><title>SEO Dashboard</title>");
        writer.write("<style>");
        writer.write("body { background-color: #ffffff; color: #000000; }"); // Set background color and text color
        writer.write("table { width: 100%; border-collapse: collapse; }");
        writer.write("table, th, td { border: 1px solid #5F008A; }"); // Border color to match text color
        writer.write("th, td { padding: 15px; text-align: left; color: #5F008A; }"); // Set text color for table headers and cells
        writer.write("th { background-color: #FFFC9F; }"); // Set background color for table headers
        writer.write("h1, h2, h3, h4, h5, h6 { color: #5F008A; }"); // Set color for all heading elements
        writer.write("</style></head><body>");
        writer.write("<h1>SEO Dashboard</h1>");
        writer.write("<p>Total pages checked: " + totalPages + "</p>");
        writer.write("<table><tr><th>SR.No</th><th>URL</th><th>Title</th><th>Description</th><th>Keywords</th><th>Canonical Link</th><th>Mobile Alternate Link</th><th>HTTPS</th><th>Headings</th><th>Spelling Errors</th></tr>");

        int srNo = 1;
        for (SEOData data : seoDataList) {
            writer.write("<tr><td>" + srNo++ + "</td>");
            writer.write("<td><a href=\"" + data.url + "\" style=\"color: #5F008A;\">" + data.url + "</a></td>");
            writer.write("<td>" + data.title + "</td>");
            writer.write("<td>" + data.description + "</td>");
            writer.write("<td>" + data.keywords + "</td>");
            writer.write("<td>" + data.canonicalLink + "</td>");
            writer.write("<td>" + data.mobileAlternateLink + "</td>");
            writer.write("<td>" + data.httpsStatus + "</td>");
            writer.write("<td>");
            for (Map.Entry<String, List<String>> entry : data.headings.entrySet()) {
                writer.write("<strong>" + entry.getKey() + ":</strong> " + String.join(", ", entry.getValue()) + "<br>");
            }
            writer.write("</td>");
            writer.write("<td>" + String.join(", ", data.spellingErrors) + "</td></tr>");
        }

        writer.write("</table></body></html>");
    }
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	
	static class SEOData {
        String url;
        String title;
        String description;
        String keywords;
        String canonicalLink;
        String mobileAlternateLink;
        Map<String, List<String>> headings;
        List<String> spellingErrors;

        SEOData(String url, String title, String description, String keywords, String canonicalLink, String mobileAlternateLink, Map<String, List<String>> headings, List<String> spellingErrors) {
            this.url = url;
            this.title = title;
            this.description = description;
            this.keywords = keywords;
            this.canonicalLink = canonicalLink;
            this.mobileAlternateLink = mobileAlternateLink;
            this.headings = headings;
            this.spellingErrors = spellingErrors;
        }
    }

    public static void main(String[] args) {
        // Set up WebDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        WebDriver driver = new ChromeDriver(options);

        // Set an implicit wait
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        try (FileWriter htmlWriter = new FileWriter("SEO_Dashboard_From_urls.html")) {

            // Read URLs from the text file
            List<String> allPageUrls = readUrlsFromFile("C:\\Users\\ashoksinh\\Desktop\\Goal 2024/Pages.txt");

            // List to hold SEO data for HTML generation
            List<SEOData> seoDataList = new ArrayList<>();

            // Loop through all collected URLs and extract SEO metadata
            for (String pageUrl : allPageUrls) {
                try {
                    driver.get(pageUrl);
                    SEOData seoData = extractSEOData(driver, pageUrl);
                    seoDataList.add(seoData);
                } catch (Exception e) {
                    System.err.println("Error processing URL: " + pageUrl);
                    e.printStackTrace();
                }
            }

            // Generate HTML dashboard
            generateHTMLDashboard(seoDataList, htmlWriter, allPageUrls.size());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.close();
        }
    }

    private static List<String> readUrlsFromFile(String fileName) throws IOException {
        List<String> urls = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                urls.add(line.trim());
            }
        }
        return urls;
    }

    private static SEOData extractSEOData(WebDriver driver, String url) {
        String title = driver.getTitle();
        String keywords = "";
        String description = "";
        String canonicalLink = "";
        String mobileAlternateLink = "";
        Map<String, List<String>> headings = new HashMap<>();
        List<String> spellingErrors = new ArrayList<>();

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
            if ("alternate".equalsIgnoreCase(linkElement.getAttribute("rel")) && "mobile".equalsIgnoreCase(linkElement.getAttribute("media"))) {
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
        List<WebElement> textElements = driver.findElements(By.xpath("//body//*[not(self::script) and not(self::style) and text()]"));
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

        return new SEOData(url, title, description, keywords, canonicalLink, mobileAlternateLink, headings, spellingErrors);
    }

    private static boolean isCorrectlySpelled(String word) {
        // Placeholder for actual spell-check logic
        // You can integrate with an API or use a dictionary for more accurate spell-checking
        return word.matches("[A-Za-z]+");
    }

    private static void generateHTMLDashboard(List<SEOData> seoDataList, FileWriter writer, int totalPages) throws IOException {
       
    	writer.write("<html><head><title>SEO Dashboard</title>");
        writer.write("<style>");
        writer.write("body { background-color: #ffffff; color: #ffffff; }"); // Set background color and text color
        writer.write("table { width: 100%; border-collapse: collapse; }");
        writer.write("table, th, td { border: 1px solid #5F008A; }"); // Border color to match text color
        writer.write("th, td { padding: 15px; text-align: left; color: #5F008A; }"); // Set text color for table headers and cells
        writer.write("th { background-color: #FFFC9F; }"); // Set background color for table headers
        writer.write("h1, h2, h3, h4, h5, h6 { color: #5F008A; }"); // Set color for all heading elements
        writer.write("</style></head><body>");
        writer.write("<h1>SEO Dashboard</h1>");
        writer.write("<p>Total pages checked: " + totalPages + "</p>");
        writer.write("<table><tr><th>SR.No</th><th>URL</th><th>Title</th><th>Description</th><th>Keywords</th><th>Canonical Link</th><th>Mobile Alternate Link</th><th>Headings</th><th>Spelling Errors</th></tr>");

        int srNo = 1;
        for (SEOData data : seoDataList) {
           
        	writer.write("<tr><td>" + srNo++ + "</td>");
            writer.write("<td><a href=\"" + data.url + "\" style=\"color: #5F008A;\">" + data.url + "</a></td>");
            writer.write("<td>" + data.title + "</td>");
            writer.write("<td>" + data.description + "</td>");
            writer.write("<td>" + data.keywords + "</td>");
            writer.write("<td>" + data.canonicalLink + "</td>");
            writer.write("<td>" + data.mobileAlternateLink + "</td>");
            writer.write("<td>");
            for (Map.Entry<String, List<String>> entry : data.headings.entrySet()) {
                writer.write("<strong>" + entry.getKey() + ":</strong> " + String.join(", ", entry.getValue()) + "<br>");
            }
            writer.write("</td>");
            writer.write("<td>" + String.join(", ", data.spellingErrors) + "</td></tr>");
        }

        writer.write("</table></body></html>");
    }
}
	
	
	*/