package my_projects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Order_Now_verification {

    WebDriver driver;
    String expectedOrderNowLink = "https://blackbeardinercatering.olo.com/";
    List<String> testResults = new ArrayList<>();
    List<String> failedPages = new ArrayList<>();

    @BeforeClass
    public void setUp() {
        // Setup WebDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();

        // Initialize ChromeOptions
        ChromeOptions options = new ChromeOptions();

        // Disable headless mode to visually inspect the browser
        // options.addArguments("--headless"); // Commented out for debugging

        // Disable images and popups for faster performance
//        options.addArguments("--blink-settings=imagesEnabled=false");
//        options.addArguments("--disable-popup-blocking");

        // Set page load strategy to NORMAL for full page loading
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        // Initialize WebDriver with options
        driver = new ChromeDriver(options);

        // Maximize the browser window for consistency
        driver.manage().window().maximize();

        // Increase page load timeout in case pages are slow to load
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
    }

    @Test
    public void checkOrderNowLinkOnPages() throws IOException {
        // Read URLs from the text file
        List<String> pageUrls = readUrlsFromFile("D:\\All Data 2024\\My Desktop\\Black Bear Diner\\21-10-2024 Order Now Link\\OrderNow.txt");

        // Explicit wait to optimize element lookups (no need for implicit waits everywhere)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Loop through each URL and check the "ORDER NOW" link
        for (String pageUrl : pageUrls) {
            try {
                // Print the URL to make sure it's being loaded
                System.out.println("Loading URL: " + pageUrl);
                
                // Navigate to the page
                driver.get(pageUrl);

                // Print the page source to check if it's loaded correctly (for debugging)
                System.out.println("Page source: " + driver.getPageSource());

                // Wait for the "ORDER NOW" link to be visible (or fail after timeout)
                List<WebElement> orderNowElements = driver.findElements(By.xpath("//a[translate(text(),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='ORDER NOW' and contains(@href, 'blackbeardinercatering.olo.com')]"));

                boolean linkFound = false;

                // Loop through all found "ORDER NOW" links to check if one has the correct URL
                for (WebElement orderNowElement : orderNowElements) {
                    String actualLink = orderNowElement.getAttribute("href");
                    if (actualLink != null && actualLink.equals(expectedOrderNowLink)) {
                        linkFound = true;
                        testResults.add("<tr><td>" + pageUrl + "</td><td style='color:green;'>PASS</td></tr>");
                        break; // Stop checking once we find a valid link
                    }
                }

                if (!linkFound) {
                    // If no correct link was found for "ORDER NOW"
                    testResults.add("<tr><td>" + pageUrl + "</td><td style='color:red;'>FAIL</td></tr>");
                    failedPages.add(pageUrl);  // Add to failed pages if the correct link is not found
                }

            } catch (Exception e) {
                testResults.add("<tr><td>" + pageUrl + "</td><td style='color:red;'>FAIL</td></tr>");
                failedPages.add(pageUrl);  // Add to failed pages if the element is not found or there's an error
                System.out.println("Error loading URL: " + pageUrl + " - " + e.getMessage()); // Log any errors
            }
        }

        // Write results to HTML file
        writeResultsToHtml("test_results.html");

        // Print failed pages in the console for developers
        if (!failedPages.isEmpty()) {
            System.out.println("The following pages do not have the correct 'ORDER NOW' link:");
            for (String failedPage : failedPages) {
                System.out.println(failedPage);
            }
        } else {
            System.out.println("All pages have the correct 'ORDER NOW' link.");
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Read URLs from a text file
    private List<String> readUrlsFromFile(String filePath) throws IOException {
        List<String> urls = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            urls.add(line);
        }
        reader.close();
        return urls;
    }

    // Write test results to an HTML file
    private void writeResultsToHtml(String outputFilePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));

        // HTML structure for the report
        writer.write("<html><head><title>Order Now Link Test Results</title></head><body>");
        writer.write("<h1>Order Now Link Test Results</h1>");
        writer.write("<table border='1'><tr><th>Page URL</th><th>Status</th></tr>");

        // Append test results
        for (String result : testResults) {
            writer.write(result);
        }

        // Close HTML structure
        writer.write("</table></body></html>");
        writer.close();
    }
}
