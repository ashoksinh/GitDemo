package New_Code;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CheckH1Css {
    private WebDriver driver;
    private List<String> urls;
    private List<String> issues;

    @BeforeClass
    public void setUp() {
        // Set the path to your WebDriver (update the path if needed)
       // System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");

        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();

        // Load URLs from the text file
        urls = loadUrlsFromFile("D:\\All Data 2024\\My Desktop\\Havis\\03-02-2025 H1 tag\\Text Transform.txt");
        issues = new ArrayList<>();
    }

    @Test
    public void verifyH1CssTextTransform() {
        for (String url : urls) {
            try {
                driver.get(url);

                // Find the H1 tag on the page
                WebElement h1Element = driver.findElement(By.tagName("h1"));

                // Get the computed CSS value for 'text-transform'
                String textTransform = h1Element.getCssValue("text-transform");

                // Check if 'text-transform' is not 'none'
                if (!textTransform.equalsIgnoreCase("none")) {
                    System.out.println("URL: " + url + " - H1 tag has 'text-transform': " + textTransform);
                    issues.add(url);
                }
            } catch (Exception e) {
                System.out.println("Error processing URL: " + url + " - " + e.getMessage());
            }
        }

        // Write URLs with issues to a file
        writeIssuesToFile("issues.txt");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Helper method to read URLs from a file
    private List<String> loadUrlsFromFile(String fileName) {
        List<String> urlList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                urlList.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urlList;
    }

    // Helper method to write issues to a file
    private void writeIssuesToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String issue : issues) {
                writer.write(issue);
                writer.newLine();
            }
            System.out.println("Issues logged in " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

