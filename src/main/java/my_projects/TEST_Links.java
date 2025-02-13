package my_projects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TEST_Links {

	
	  WebDriver driver;
	    WebDriverWait wait;
	    String filePath = "C:\\Users\\ashoksinh\\Desktop\\ULR.txt"; // Replace with the path to your text file
	    String[] targetURLs = {
	            "https://havismaindev.wpenginepowered.com/collections/",
	            "https://havismaindev.wpenginepowered.com/md-501-rugged-warehouse-logistics-collection/",
	            "https://havismaindev.wpenginepowered.com/chevy-vsx-collection/",
	            "https://havismaindev.wpenginepowered.com/2020-22-ford-interceptor-collection/",
	            "https://havismaindev.wpenginepowered.com/2021-22-chevrolet-tahoe-collection/",
	            "https://havismaindev.wpenginepowered.com/ford-vsx-collection/"
	           	    };

	    @BeforeClass
	    public void setup() {
	      //  System.setProperty("webdriver.chrome.driver", "path/to/chromedriver"); // Replace with the path to your ChromeDriver
	        driver = new ChromeDriver();
	        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        driver.manage().window().maximize();
	    }

	    @Test
	    public void checkURLs() throws IOException {
	        Set<String> urls = readURLsFromFile(filePath);

	        for (String url : urls) {
	            driver.navigate().to(url);
	            wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
	            checkTargetURLs(url);
	        }
	    }

	    public Set<String> readURLsFromFile(String filePath) throws IOException {
	        Set<String> urls = new HashSet<>();
	        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	            String line;
	            while ((line = br.readLine()) != null) {
	                urls.add(line.trim());
	            }
	        }
	        return urls;
	    }

	    public void checkTargetURLs(String pageURL) {
	        for (String targetURL : targetURLs) {
	            if (isURLPresent(targetURL)) {
	                System.out.println("URL Found: " + targetURL + " on page " + pageURL);
	            } else {
	                System.out.println("URL Not Found: " + targetURL + " on page " + pageURL);
	            }
	        }
	    }

	    public boolean isURLPresent(String targetURL) {
	        List<WebElement> allLinks = driver.findElements(By.tagName("a"));
	        for (WebElement link : allLinks) {
	            if (targetURL.equals(link.getAttribute("href"))) {
	                return true;
	            }
	        }
	        return false;
	    }

	    @AfterClass
	    public void tearDown() {
	        if (driver != null) {
	            driver.quit();
	        }
	    }

	    public static void main(String[] args) throws IOException {
	    	TEST_Links checker = new TEST_Links();
	        checker.setup();
	        checker.checkURLs();
	        checker.tearDown();
	    }
	}



	
	
	
	/*
	 WebDriver driver;
	    WebDriverWait wait;
	    String mainURL = "https://havismaindev.wpenginepowered.com/"; // Replace with your main URL
	    String[] targetURLs = {
	            "https://havismaindev.wpenginepowered.com/collections/",
	            "https://havismaindev.wpenginepowered.com/md-501-rugged-warehouse-logistics-collection/",
	            "https://havismaindev.wpenginepowered.com/chevy-vsx-collection/",
	            "https://havismaindev.wpenginepowered.com/2020-22-ford-interceptor-collection/",
	            "https://havismaindev.wpenginepowered.com/2021-22-chevrolet-tahoe-collection/",
	            "https://havismaindev.wpenginepowered.com/ford-vsx-collection/"
	    };

	    @BeforeClass
	    public void setup() {
	       // System.setProperty("webdriver.chrome.driver", "path/to/chromedriver"); // Replace with the path to your ChromeDriver
	        driver = new ChromeDriver();
	        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        driver.manage().window().maximize();
	    }

	    @Test
	    public void checkURLs() {
	        driver.get(mainURL);
	        List<WebElement> links = driver.findElements(By.tagName("a"));
	        Set<String> uniqueLinks = new HashSet<>();

	        // Store unique links to avoid rechecking the same link
	        for (WebElement link : links) {
	            String url = link.getAttribute("href");
	            if (url != null && !url.isEmpty() && url.startsWith(mainURL)) {
	                uniqueLinks.add(url);
	            }
	        }

	        for (String url : uniqueLinks) {
	            driver.navigate().to(url);
	            wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
	            checkTargetURLs();
	            driver.navigate().back();
	            wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
	        }
	    }

	    public void checkTargetURLs() {
	        for (String targetURL : targetURLs) {
	            if (isURLPresent(targetURL)) {
	                System.out.println("URL Found: " + targetURL);
	            } else {
	                System.out.println("URL Not Found: " + targetURL);
	            }
	        }
	    }

	    public boolean isURLPresent(String targetURL) {
	        List<WebElement> allLinks = driver.findElements(By.tagName("a"));
	        for (WebElement link : allLinks) {
	            if (targetURL.equals(link.getAttribute("href"))) {
	                return true;
	            }
	        }
	        return false;
	    }

	    @AfterClass
	    public void tearDown() {
	        if (driver != null) {
	            driver.quit();
	        }
	    }
	}
	*/