package my_projects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SEO_Meta_Single_Site {



	    private WebDriver driver;
	    private List<String> stageUrls;

	    @BeforeClass
	    public void setup() {
	        // Set Chrome options
	        ChromeOptions options = new ChromeOptions();
	        options.addArguments("--no-sandbox");
	        options.addArguments("--disable-dev-shm-usage");

	        driver = new ChromeDriver(options);

	        // Define file path for staging URLs
	        String stagingUrlsFile = "D:\\All Data 2024\\My Desktop\\Primal Bod\\All Pages.txt";

	        // Check if the file exists
	        File stageFile = new File(stagingUrlsFile);
	        if (!stageFile.exists()) {
	            throw new RuntimeException("File not found: " + stagingUrlsFile);
	        }

	        // Read URLs from the staging file
	        stageUrls = readUrlsFromFile(stagingUrlsFile);
	    }

	    @Test
	    public void compareMetaTags() {
	        try (FileWriter writer = new FileWriter("SEO_Meta_Report_Single_Site.html")) {
	            writer.write("<html><head><style>");
	            writer.write("body {font-family: Arial, sans-serif; margin: 20px;}");
	            writer.write("table {width: 100%; border-collapse: collapse;}");
	            writer.write("th, td {border: 1px solid black; padding: 10px; text-align: left;}");
	            writer.write("th {background-color: #4CAF50; color: white;}");
	            writer.write("td.warning {background-color: #fff3cd; color: #856404; font-weight: bold;}");
	            writer.write("</style></head><body>");
	            writer.write("<h2>SEO Meta Tag Report</h2>");
	            writer.write("<table>");
	            writer.write("<tr><th>Sr No.</th><th>Stage URL</th><th>Meta Title</th><th>Meta Description</th></tr>");

	            int srNo = 1;

	            for (String stageUrl : stageUrls) {
	                System.out.println("Processing URL: " + stageUrl);

	                // Get meta tags
	                String[] stageMeta = getMetaTags(driver, stageUrl);

	                // Determine if tags are missing
	                String title = stageMeta[0].equals("No title found") ? "<span class='warning'>No title found</span>" : stageMeta[0];
	                String description = stageMeta[1].equals("No description found") ? "<span class='warning'>No description found</span>" : stageMeta[1];

	                // Write data to the report
	                writer.write("<tr>");
	                writer.write("<td>" + srNo++ + "</td>");
	                writer.write("<td>" + stageUrl + "</td>");
	                writer.write("<td>" + title + "</td>");
	                writer.write("<td>" + description + "</td>");
	                writer.write("</tr>");
	            }

	            writer.write("</table></body></html>");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    @AfterClass
	    public void teardown() {
	        if (driver != null) {
	            driver.quit();
	        }
	    }

	    private List<String> readUrlsFromFile(String filePath) {
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
	            throw new RuntimeException("Error reading file: " + filePath);
	        }

	        return urlList;
	    }

	    private String[] getMetaTags(WebDriver driver, String url) {
	        try {
	            driver.get(url);
	            Thread.sleep(2000);  // Replace with WebDriverWait if needed

	            String title = driver.getTitle();
	            String description;

	            try {
	                description = driver.findElement(By.cssSelector("meta[name='description']")).getAttribute("content");
	            } catch (Exception e) {
	                description = "No description found";
	            }

	            return new String[]{
	                    title.isEmpty() ? "No title found" : title,
	                    description.isEmpty() ? "No description found" : description
	            };
	        } catch (Exception e) {
	            System.out.println("Error fetching meta tags for URL: " + url + " - " + e.getMessage());
	            return new String[]{"No title found", "No description found"};
	        }
	    }
	}