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

public class Pages_404_Test {

    private WebDriver driver;
    private Set<String> crawledPages = new HashSet<>(); // Store all unique crawled pages
    private List<Map<String, String>> error404Pages = new ArrayList<>(); // Store 404 pages with parent page and link text
    private List<Map.Entry<String, String>> specialSchemeLinks = new ArrayList<>(); // Store links with special schemes like tel, mailto, etc.
    private List<Map.Entry<String, String>> thirdPartyLinks = new ArrayList<>(); // Store third-party links with parent page

    private Map<String, String> linkOriginMap = new HashMap<>(); // Store each link and the parent page where it was found

    @BeforeTest
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public void testCrawl404Pages() throws IOException {
        String baseUrl = "https://blackbeardine2.wpenginepowered.com/"; // Homepage URL to start crawling
        Set<String> visitedPages = new HashSet<>(); // To track visited pages and avoid duplicates

        // Start crawling from homepage
        crawlPages(baseUrl, baseUrl, visitedPages);

        // Validate that some pages were crawled
        Assert.assertTrue(crawledPages.size() > 0, "No pages were crawled.");

        // Generate HTML report
        generateHTMLReport();
    }

    /**
     * Crawl the website recursively, extracting all links from the given page and checking for 404 errors.
     */
    private void crawlPages(String pageUrl, String baseUrl, Set<String> visitedPages) {
        if (visitedPages.contains(pageUrl)) {
            return; // Avoid crawling the same page multiple times
        }

        visitedPages.add(pageUrl); // Mark this page as visited

        try {
            // Handle relative URLs
            URL absoluteUrl = new URL(baseUrl);
            URL fullUrl = new URL(absoluteUrl, pageUrl); // Convert relative to absolute URL

            // Check if the page exists and doesn't return a 404 error
            int statusCode = getHttpStatus(fullUrl.toString());
            if (statusCode == 404) {
                String linkText = driver.findElement(By.linkText(pageUrl)).getText();
                Map<String, String> errorPageDetails = new HashMap<>();
                errorPageDetails.put("url", fullUrl.toString());
                errorPageDetails.put("parent", linkOriginMap.get(fullUrl.toString()));
                errorPageDetails.put("linkText", linkText); // Store the link text where the user clicked
                error404Pages.add(errorPageDetails);
            } else {
                crawledPages.add(fullUrl.toString()); // Store all valid pages

                // Navigate to the page
                driver.get(fullUrl.toString());

                // Wait until all links are present on the page
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("a")));

                // Extract all anchor links
                List<WebElement> links = driver.findElements(By.tagName("a"));

                for (WebElement link : links) {
                    String href = link.getAttribute("href");
                    String linkText = link.getText(); // Capture the link text

                    // Skip null, "javascript:void(0);", and any links containing a "#"
                    if (href == null || href.equals("javascript:void(0);") || href.contains("#")) {
                        continue; // Do not include these links in the crawl or report
                    }

                    // Check if the link starts with specific schemes: http, https, www, mailto, tel, fax
                    if (isSpecialScheme(href)) {
                        specialSchemeLinks.add(new AbstractMap.SimpleEntry<>(href, fullUrl.toString())); // List links with special schemes
                    } else if (!isThirdPartyLink(href, baseUrl)) {
                        linkOriginMap.put(href, fullUrl.toString()); // Track parent page for each internal link
                        // Recursively crawl internal links (not third-party or image/PDF)
                        crawlPages(href, baseUrl, visitedPages);
                    } else if (isThirdPartyLink(href, baseUrl)) {
                        thirdPartyLinks.add(new AbstractMap.SimpleEntry<>(href, fullUrl.toString())); // List third-party links with parent
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error processing URL: " + pageUrl);
            e.printStackTrace();
        }
    }

    /**
     * Get HTTP status code for a given URL.
     */
    private int getHttpStatus(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        return connection.getResponseCode();
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Determine if a link is a third-party link (external).
     */
    private boolean isThirdPartyLink(String url, String baseUrl) {
        return !url.startsWith(baseUrl);
    }

    /**
     * Determine if a link has a special scheme (http, https, mailto, tel, fax).
     */
    private boolean isSpecialScheme(String url) {
        return url.startsWith("http") || url.startsWith("https") || url.startsWith("www")
            || url.startsWith("mailto") || url.startsWith("tel") || url.startsWith("fax");
    }

    /**
     * Generate an HTML report of all pages crawled, 404 errors, special scheme links (mailto, tel, fax), and third-party links.
     */
    private void generateHTMLReport() throws IOException {
        try (FileWriter writer = new FileWriter("Crawled_Pages_Report.html")) {
            writer.write("<html><head><title>Crawled Pages Report</title>");
            writer.write("<link href=\"https://fonts.googleapis.com/css2?family=Lexend&display=swap\" rel=\"stylesheet\">");
            writer.write("<style>");
            writer.write("body { background-color: #ffffff; color: #000000; font-family: 'Lexend', sans-serif; }");
            writer.write("table { width: 100%; border-collapse: collapse; font-family: 'Lexend', sans-serif; }");
            writer.write("table, th, td { border: 1px solid #5F008A; }");
            writer.write("th, td { padding: 15px; text-align: left; color: #5F008A; font-family: 'Lexend', sans-serif; }");
            writer.write("th { background-color: #FFFC9F; }");
            writer.write("h1, h2, h3, h4, h5, h6 { color: #5F008A; font-family: 'Lexend', sans-serif; text-align: center; }");
            writer.write("a { color: #5F008A; text-decoration: none; }");
            writer.write("a:hover { color: #000000; }"); // Hover effect changes color to black
            writer.write("</style></head><body>");
            writer.write("<h1>Crawled Pages Report</h1>");

            // List all 404 pages with clicked link text
            writer.write("<h2>404 Error Pages</h2>");
            writer.write("<table><tr><th>SR.No</th><th>404 URL</th><th>Clicked From (Parent URL)</th><th>Link Text</th><th>Status</th><th>Type</th><th>Size</th><th>Date</th></tr>");
            int srNo404 = 1;
            for (Map<String, String> errorPage : error404Pages) {
                writer.write("<tr><td>" + srNo404++ + "</td>");
                writer.write("<td><a href=\"" + errorPage.get("url") + "\" target=\"_blank\">" + errorPage.get("url") + "</a></td>");
                writer.write("<td><a href=\"" + errorPage.get("parent") + "\" target=\"_blank\">" + errorPage.get("parent") + "</a></td>");
                writer.write("<td>" + errorPage.get("linkText") + "</td>");
                writer.write("<td>404</td>");  // Assuming status is always 404
                writer.write("<td>text/html</td>"); // Placeholder, you can update this logic
                writer.write("<td>N/A</td>"); // Placeholder for size
                writer.write("<td>N/A</td></tr>"); // Placeholder for date
            }
            writer.write("</table>");

            // You can update similar logic for crawled pages, special scheme links, and third-party links
            // List all crawled pages
            writer.write("<h2>All Crawled Pages</h2>");
            writer.write("<table><tr><th>SR.No</th><th>URL</th><th>Status</th><th>Type</th><th>Size</th><th>Date</th></tr>");
            int srNoCrawled = 1;
            for (String page : crawledPages) {
                writer.write("<tr><td>" + srNoCrawled++ + "</td>");
                writer.write("<td><a href=\"" + page + "\" target=\"_blank\">" + page + "</a></td>");
                writer.write("<td>200</td>"); // Placeholder for status
                writer.write("<td>text/html</td>"); // Placeholder for type
                writer.write("<td>N/A</td>"); // Placeholder for size
                writer.write("<td>N/A</td></tr>"); // Placeholder for date
            }
            writer.write("</table>");

            // Continue with sections for special scheme links and third-party links...
            writer.write("</body></html>");
        }
    }
}















































/*




import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.Assert;

import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class Pages_404_Test {

    private WebDriver driver;
    private List<String> crawledPages = new ArrayList<>(); // Store all crawled pages
    private List<String> error404Pages = new ArrayList<>(); // Store 404 pages
    private List<String> imageAndPdfLinks = new ArrayList<>(); // Store image and PDF links
    private List<String> thirdPartyLinks = new ArrayList<>(); // Store third-party links

    @BeforeTest
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public void testCrawl404Pages() throws IOException {
        String baseUrl = "https://blackbeardine2.wpenginepowered.com/"; // Homepage URL to start crawling
        Set<String> allPages = new HashSet<>(); // To track visited pages and avoid duplicates

        // Start crawling from homepage
        crawlPages(baseUrl, baseUrl, allPages);

        // Validate that some pages were crawled
        Assert.assertTrue(crawledPages.size() > 0, "No pages were crawled.");

        // Generate HTML report
        generateHTMLReport();
    }

   
    private void crawlPages(String pageUrl, String baseUrl, Set<String> visitedPages) {
        if (visitedPages.contains(pageUrl)) {
            return; // Avoid crawling the same page multiple times
        }

        visitedPages.add(pageUrl); // Mark this page as visited

        try {
            // Check if the page exists and doesn't return a 404 error
            int statusCode = getHttpStatus(pageUrl);
            if (statusCode == 404) {
                error404Pages.add(pageUrl); // Store the 404 page
            } else {
                crawledPages.add(pageUrl); // Store all valid pages

                // Navigate to the page
                driver.get(pageUrl);

                // Extract all anchor links
                List<WebElement> links = driver.findElements(By.tagName("a"));
                for (WebElement link : links) {
                    String href = link.getAttribute("href");

                    // Skip null links
                    if (href == null) {
                        continue;
                    }

                    // Skip links that are images, PDFs or contain "#" and list them separately
                    if (isImageOrPdf(href)) {
                        imageAndPdfLinks.add(href); // List image and PDF links
                    } else if (!href.contains("#") && !isThirdPartyLink(href, baseUrl)) {
                        // Recursively crawl internal links (not third-party or image/PDF)
                        crawlPages(href, baseUrl, visitedPages);
                    } else if (isThirdPartyLink(href, baseUrl)) {
                        thirdPartyLinks.add(href); // List third-party links
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error processing URL: " + pageUrl);
            e.printStackTrace();
        }
    }

   
   //  * Get HTTP status code for a given URL.
   
    private int getHttpStatus(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        return connection.getResponseCode();
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    
    // Determine if a link is an image or PDF.
     
    private boolean isImageOrPdf(String url) {
        String[] imageExtensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".svg"};
        String[] pdfExtensions = {".pdf"};

        for (String ext : imageExtensions) {
            if (url.toLowerCase().endsWith(ext)) {
                return true;
            }
        }
        for (String ext : pdfExtensions) {
            if (url.toLowerCase().endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

     // Determine if a link is a third-party link (external).
    
    private boolean isThirdPartyLink(String url, String baseUrl) {
        return !url.startsWith(baseUrl);
    }

  
   // Generate an HTML report of all pages crawled, 404 errors, images/PDFs, and third-party links.
     
    private void generateHTMLReport() throws IOException {
        try (FileWriter writer = new FileWriter("Crawled_Pages_Report.html")) {
            writer.write("<html><head><title>Crawled Pages Report</title>");
            writer.write("<link href=\"https://fonts.googleapis.com/css2?family=Lexend&display=swap\" rel=\"stylesheet\">");
            writer.write("<style>");
            writer.write("body { background-color: #ffffff; color: #000000; font-family: 'Lexend', sans-serif; }");
            writer.write("table { width: 100%; border-collapse: collapse; font-family: 'Lexend', sans-serif; }");
            writer.write("table, th, td { border: 1px solid #5F008A; }");
            writer.write("th, td { padding: 15px; text-align: left; color: #5F008A; font-family: 'Lexend', sans-serif; }");
            writer.write("th { background-color: #FFFC9F; }");
            writer.write("h1, h2, h3, h4, h5, h6 { color: #5F008A; font-family: 'Lexend', sans-serif; text-align: center; }");
            writer.write("</style></head><body>");
            writer.write("<h1>Crawled Pages Report</h1>");

            // List all 404 pages
            writer.write("<h2>404 Error Pages</h2>");
            writer.write("<table><tr><th>SR.No</th><th>404 URL</th></tr>");
            int srNo404 = 1;
            for (String errorPage : error404Pages) {
                writer.write("<tr><td>" + srNo404++ + "</td><td>" + errorPage + "</td></tr>");
            }
            writer.write("</table>");

            // List all crawled pages
            writer.write("<h2>All Crawled Pages</h2>");
            writer.write("<table><tr><th>SR.No</th><th>URL</th></tr>");
            int srNoCrawled = 1;
            for (String page : crawledPages) {
                writer.write("<tr><td>" + srNoCrawled++ + "</td><td>" + page + "</td></tr>");
            }
            writer.write("</table>");

            // List all image and PDF links
            writer.write("<h2>Image and PDF Links</h2>");
            writer.write("<table><tr><th>SR.No</th><th>Image/PDF URL</th></tr>");
            int srNoImagePdf = 1;
            for (String link : imageAndPdfLinks) {
                writer.write("<tr><td>" + srNoImagePdf++ + "</td><td>" + link + "</td></tr>");
            }
            writer.write("</table>");

            // List all third-party links
            writer.write("<h2>Third-Party Links</h2>");
            writer.write("<table><tr><th>SR.No</th><th>Third-Party URL</th></tr>");
            int srNoThirdParty = 1;
            for (String link : thirdPartyLinks) {
                writer.write("<tr><td>" + srNoThirdParty++ + "</td><td>" + link + "</td></tr>");
            }
            writer.write("</table>");

            writer.write("</body></html>");
        }
    }
}

*/