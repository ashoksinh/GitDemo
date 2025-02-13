package my_projects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Location_titles {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		// System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
	        WebDriver driver = new ChromeDriver();
	        
	       

	        List<String> stageUrls = new ArrayList<>();
	        List<String> liveUrls = new ArrayList<>();

	        // Add URLs (Make sure the stage and live URLs are in the same order)
	        stageUrls.add("https://demo:Wliq@1.2.3@blackbeardine1.wpenginepowered.com/locations/");
	        liveUrls.add("https://blackbeardiner.com/locations/");
	        // Add more URLs as needed

	        // Prepare to collect results
	        StringBuilder htmlResult = new StringBuilder();
	        htmlResult.append("<html><head><title>Title Comparison Results</title></head><body>");
	        htmlResult.append("<h1>Comparison Results</h1>");
	        htmlResult.append("<table border='1'><tr><th>Sr No.</th><th>Stage Title</th><th>Live Title</th><th>Status</th></tr>");

	        // Loop through URLs and compare titles
	        for (int i = 0; i < stageUrls.size(); i++) {
	            // Navigate to stage URL and get elements using XPath
	            driver.get(stageUrls.get(i));
	            List<WebElement> stageElements = driver.findElements(By.xpath("//li[@class='pt30']/a"));
	            List<String> stageTitles = new ArrayList<>();
	            for (WebElement element : stageElements) {
	                stageTitles.add(element.getText());
	            }
	            Collections.sort(stageTitles);  // Sort stage titles in ascending order

	            // Navigate to live URL and get elements using XPath
	            driver.get(liveUrls.get(i));
	            List<WebElement> liveElements = driver.findElements(By.xpath("//div[@class='thumbnail']//h3"));
	            List<String> liveTitles = new ArrayList<>();
	            for (WebElement element : liveElements) {
	                liveTitles.add(element.getText());
	            }
	            Collections.sort(liveTitles);  // Sort live titles in ascending order

	            // Determine the maximum number of rows needed for the table
	            int maxRows = Math.max(stageTitles.size(), liveTitles.size());

	            // Loop to add each row of titles to the HTML table
	            for (int j = 0; j < maxRows; j++) {
	                String stageTitle = j < stageTitles.size() ? stageTitles.get(j) : "";
	                String liveTitle = j < liveTitles.size() ? liveTitles.get(j) : "";
	                String status = stageTitle.equals(liveTitle) ? "Matched" : "Unmatched";
	                String statusColor = stageTitle.equals(liveTitle) ? "green" : "red";

	                // Append result to HTML
	                htmlResult.append("<tr>")
	                          .append("<td>").append(i + 1).append(".").append(j + 1).append("</td>")
	                          .append("<td>").append(stageTitle).append("</td>")
	                          .append("<td>").append(liveTitle).append("</td>")
	                          .append("<td style='color:").append(statusColor).append(";'>").append(status).append("</td>")
	                          .append("</tr>");
	            }
	        }

	        // Finalize HTML
	        htmlResult.append("</table></body></html>");

	        // Write HTML to file
	        try (FileWriter writer = new FileWriter("TitleComparisonResults.html")) {
	            writer.write(htmlResult.toString());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        // Close the browser
	        driver.quit();
	    }
	}