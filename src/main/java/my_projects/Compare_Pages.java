package my_projects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Compare_Pages {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub

		WebDriverManager.chromedriver().setup();
    	WebDriver driver = new ChromeDriver();		

        List<String> devUrls = readUrlsFromFile("D:\\Ashoksinh Data\\My Desktop\\Wic Resources\\West Virginia\\Dev URL.txt");
        List<String> liveUrls = readUrlsFromFile("D:\\Ashoksinh Data\\My Desktop\\Wic Resources\\West Virginia\\Live site.txt");

        if (devUrls.size() != liveUrls.size()) {
            System.err.println("URL lists have different lengths!");
            return;
        }

        for (int i = 0; i < devUrls.size(); i++) {
            String devUrl = devUrls.get(i);
            String liveUrl = liveUrls.get(i);

            driver.get(devUrl);
            String devTitle = driver.getTitle();
            List<WebElement> devLinks = driver.findElements(By.tagName("a"));
            List<WebElement> devHeaders = driver.findElements(By.tagName("h1"));

            driver.get(liveUrl);
            String liveTitle = driver.getTitle();
            List<WebElement> liveLinks = driver.findElements(By.tagName("a"));
            List<WebElement> liveHeaders = driver.findElements(By.tagName("h1"));

            // Compare titles
            if (!devTitle.equals(liveTitle)) {
                System.out.println("Title mismatch on index " + i + ": Dev (" + devTitle + ") - Live (" + liveTitle + ")");
            }

            // Compare the number of links
            if (devLinks.size() != liveLinks.size()) {
                System.out.println("Link count mismatch on index " + i + ": Dev (" + devLinks.size() + ") - Live (" + liveLinks.size() + ")");
            }

            // Compare the number of headers
            if (devHeaders.size() != liveHeaders.size()) {
                System.out.println("Header count mismatch on index " + i + ": Dev (" + devHeaders.size() + ") - Live (" + liveHeaders.size() + ")");
            }

            // Additional comparisons can be added here
        }

        driver.close();
    }

    private static List<String> readUrlsFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            return reader.lines().toList();
        }
    }
}