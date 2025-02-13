package my_projects;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Banner_image {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		WebDriverManager.chromedriver().setup();
    	WebDriver driver = new ChromeDriver();

          //String filePath = "D:\\Ashoksinh Data\\My Desktop\\Offerpad\\All pages .txt";
          String filePath = "D:\\Ashoksinh Data\\My Desktop\\Offerpad\\All Pages Website .txt";
          
          List<String> websiteURLs = readURLsFromFile(filePath);

        // List to store pages with banner images
        List<String> pagesWithBannerImages = new ArrayList<>();

        // List to store pages without banner images
        List<String> pagesWithoutBannerImages = new ArrayList<>();

        // List to store 404 pages
        List<String> pagesNotFound = new ArrayList<>();

        // List to store pages with map images
        List<String> pagesWithMapImages = new ArrayList<>();

        // List to store pages without map images
        List<String> pagesWithoutMapImages = new ArrayList<>();

        // Loop through each website URL
        for (String websiteURL : websiteURLs) {
            // Navigate to the website URL
            driver.get(websiteURL);

            // Check if page returns 404
            if (driver.getPageSource().contains("404 ")) {
                pagesNotFound.add(websiteURL);
                continue; // Move to next page
            }

            boolean hasBanner = false;
            boolean hasMap = false;

            // Check if banner image exists
            try {
                WebElement bannerImage = driver.findElement((By.xpath("//*[contains(concat( \" \", @class, \" \" ), concat( \" \", \"location-banner\", \" \" ))]")));
                if (bannerImage != null) {
                    hasBanner = true;
                    pagesWithBannerImages.add(websiteURL);
                }
            } catch (Exception e) {
                // Banner image not found
            }

            // Check if map image section exists
            try {
                WebElement mapImage = driver.findElement(By.xpath("//img"));
                if (mapImage != null) {
                    hasMap = true;
                    pagesWithMapImages.add(websiteURL);
                }
            } catch (Exception e) {
                // Map image section not found
            }

            // Add to pages without banner images list
            if (!hasBanner) {
                pagesWithoutBannerImages.add(websiteURL);
            }

            // Add to pages without map images list
            if (!hasMap) {
                pagesWithoutMapImages.add(websiteURL);
            }
        }

        // Write pages with banner images to a text file
        writeToFile("Pages_with_banner_images.txt", pagesWithBannerImages);

        // Write 404 pages to a text file
        writeToFile("404_pages.txt", pagesNotFound);

        // Write pages with map images to a text file
        writeToFile("Pages_with_map_images.txt", pagesWithMapImages);

        // Write pages without banner images to a text file
        writeToFile("Pages_without_banner_images.txt", pagesWithoutBannerImages);

        // Write pages without map images to a text file
        writeToFile("Pages_without_map_images.txt", pagesWithoutMapImages);

        // Quit the WebDriver
    
    
	}

	private static List<String> readURLsFromFile(String filePath) {
		List<String> urls = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				urls.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return urls;
	}

	private static void writeToFile(String filename, List<String> pages) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String page : pages) {
                writer.write(page);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	}

