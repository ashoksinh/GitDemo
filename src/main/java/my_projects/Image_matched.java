package my_projects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Image_matched {
	   public static void main(String[] args) {
	        // Set up the Chrome WebDriver
	       // System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
		   WebDriver driver = new ChromeDriver();
	        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	        
	        
	        // Replace these with the actual list of live and staging URLs
	        String[] liveURLs = { "https://blackbeardiner.com/172-black-bear-diner/"};
	        String[] stagingURLs = { "https://demo:Wliq@1.2.3@blackbeardine1.wpenginepowered.com/172-black-bear-diner/"};


	        try {
	            BufferedWriter writer = new BufferedWriter(new FileWriter("image-comparison-result.html"));
	            writer.write("<html><head><title>Image Comparison Report</title></head><body>");
	            writer.write("<table border='1'><tr><th>Live URL</th><th>Staging URL</th><th>Result</th></tr>");

	            for (int i = 0; i < liveURLs.length; i++) {
	                String liveURL = liveURLs[i];
	                String stagingURL = stagingURLs[i];

	                // Fetch the live site image URL
	                driver.get(liveURL);
	                WebElement liveImage = driver.findElement(By.xpath("(//img)[3]"));
	                String liveImageURL = liveImage.getAttribute("src");
	                System.out.println("Live Image URL: " + liveImageURL);

	                // Fetch the staging site image URL
	                driver.get(stagingURL);
	                // Ensure that the third image is correctly targeted
	                List<WebElement> stagingImages = driver.findElements(By.xpath("(//div[@class='elementor-widget-container']//img)[position()=3]"));
	                String stagingImageURL = (stagingImages.size() >= 3) ? stagingImages.get(2).getAttribute("src") : "Image not found";
	                System.out.println("Staging Image URL: " + stagingImageURL);

	                // Compare image URLs and write the result
	                if (liveImageURL.equals(stagingImageURL)) {
	                    writer.write("<tr><td>" + liveURL + "</td><td>" + stagingURL + "</td><td>Image Matched</td></tr>");
	                } else {
	                    writer.write("<tr><td>" + liveURL + "</td><td>" + stagingURL + "</td><td>Image Unmatched</td></tr>");
	                }
	            }

	            writer.write("</table></body></html>");
	            writer.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            //driver.quit();
	        }
	    }
	}