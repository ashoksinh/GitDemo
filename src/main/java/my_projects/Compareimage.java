package my_projects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;

import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Compareimage {

	private WebDriver driver;
	//private Eyes eyes;

		public void set() {
			// Set the path to your ChromeDriver executable
			 WebDriverManager.firefoxdriver().setup();
			 driver = new FirefoxDriver();
//			 driver.get("https://staging.freenotesharmonypark.com/");
//			 driver.get("https://freenotesharmonypark.com/");
		}

		 @Test
		    public void compareLayouts() {
		        driver.get("your_stage_site_url");
		        captureScreenshot("stage_page.png");

		        driver.get("your_live_site_url");
		        captureScreenshot("live_page.png");
		    }

		    @AfterClass
		    public void tearDown() {
		        driver.quit();
		    }

		    private void captureScreenshot(String fileName) {
		        WebElement body = driver.findElement(By.tagName("body"));
		        File screenshot = body.getScreenshotAs(org.openqa.selenium.OutputType.FILE);

		        try {
		            BufferedImage fullImg = ImageIO.read(screenshot);
		            ImageIO.write(fullImg, "png", new File(fileName));
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		}