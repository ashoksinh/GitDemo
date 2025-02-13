package my_projects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Get_URLs_From_Page {

    public class GetSpecificLocationURLsTest {

        private WebDriver driver;

        @BeforeClass
        public void setUp() {
            // Set the path for your WebDriver, if needed
            driver = new ChromeDriver();
            driver.manage().window().maximize();
        }

        @Test
        public void getLocationURLs() {
            // Navigate to the target page
            driver.get("https://demo:Wliq@1.2.3@blackbeardine1.wpenginepowered.com/locations/");
            
            // Find all anchor tags on the page
            List<WebElement> links = driver.findElements(By.tagName("a"));
            
            // Prepare HTML content with a table, background color, and heading
            StringBuilder htmlContent = new StringBuilder();
            htmlContent.append("<html><head><title>Location URLs</title></head>");
            htmlContent.append("<body style='font-family: Arial, sans-serif; background-color: #f4f4f9;'>");
            htmlContent.append("<h1 style='color: #333;'>Location URLs</h1>");
            htmlContent.append("<table style='width: 100%; border-collapse: collapse; background-color: white;'>");
            htmlContent.append("<thead><tr style='background-color: #FFFC9F; color: #5F008A;'>");
            htmlContent.append("<th style='padding: 8px; text-align: left;'>URL</th></tr></thead><tbody>");
            
            // Loop through each link, filter for the /location/{city-name} pattern
            for (WebElement link : links) {
                String url = link.getAttribute("href");
                if (url != null && url.contains("/location/") && !url.endsWith("#")) {
                    htmlContent.append("<tr><td style='padding: 8px; border: 1px solid #ddd;'>");
                    htmlContent.append("<a href=\"").append(url).append("\" style='color: #5F008A;'>").append(url).append("</a>");
                    htmlContent.append("</td></tr>");
                }
            }

            // Close the table and body tags
            htmlContent.append("</tbody></table></body></html>");

            // Write the HTML content to a file
            try (FileWriter fileWriter = new FileWriter("Get_URLs_From_Page.html")) {
                fileWriter.write(htmlContent.toString());
                System.out.println("URLs have been written to LocationURLs.html in table format.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @AfterClass
        public void tearDown() {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
