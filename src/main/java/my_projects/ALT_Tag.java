
package my_projects;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class ALT_Tag {

    private WebDriver driver;
    private BufferedWriter writerWithAlt;
    private BufferedWriter writerWithoutAlt;
    private static final String FILE_PATH = "D:\\All Data 2024\\My Desktop\\TSIB\\alt.txt";

    @BeforeClass
    public void setUp() throws IOException {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));

        writerWithAlt = new BufferedWriter(new FileWriter("withAlt.txt"));
        writerWithoutAlt = new BufferedWriter(new FileWriter("withoutAlt.txt"));
        
		WebDriverWait wait= new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#toast-container")));
    }

    @DataProvider(name = "urlProvider")
    public Object[] urlProvider() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
        return reader.lines().toArray();
    }

    @Test(dataProvider = "urlProvider")
    public void checkAltTags(String url) throws IOException {
        driver.get(url);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("img")));

        List<WebElement> images = driver.findElements(By.tagName("img"));

        boolean foundImageWithoutAlt = false;
        boolean foundImageWithAlt = false;

        for (WebElement image : images) {
            String alt = image.getAttribute("alt");
            String src = image.getAttribute("src");

            if (alt == null || alt.isEmpty()) {
                if (!foundImageWithoutAlt) {
                    writerWithoutAlt.write("\n" + "Page URL: " + url + "\n");
                    foundImageWithoutAlt = true;
                }
                writerWithoutAlt.write(src + "\n");
            } else {
                if (!foundImageWithAlt) {
                    writerWithAlt.write("\n" + "Page URL: " + url + "\n");
                    foundImageWithAlt = true;
                }
                writerWithAlt.write("Image with alt attribute found: " + src + " (alt: " + alt + ")\n");
            }
        }
    }

    @AfterClass
    public void tearDown() throws IOException {
        if (driver != null) {
            driver.quit();
        }
        if (writerWithAlt != null) {
            writerWithAlt.close();
        }
        if (writerWithoutAlt != null) {
            writerWithoutAlt.close();
        }
    }
}




























/*
package my_projects;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;

public class ALT_Tag {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

	 	WebDriverManager.chromedriver().setup();
    	WebDriver driver = new ChromeDriver();

    	
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    	// Path to your file with URLs
        String filePath = "D:\\All Data 2024\\My Desktop\\Fuel for Brands\\Alt.txt";
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        // Writers for withAlt.txt and withoutAlt.txt
        BufferedWriter writerWithAlt = new BufferedWriter(new FileWriter("withAlt.txt"));
        BufferedWriter writerWithoutAlt = new BufferedWriter(new FileWriter("withoutAlt.txt"));

        String url;
        while ((url = reader.readLine()) != null) {
            // Visit each URL
            driver.get(url);

            // Wait until the images are loaded
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("img")));

            // Find all images on the page
            List<WebElement> images = driver.findElements(By.tagName("img"));

            boolean foundImageWithoutAlt = false;
            boolean foundImageWithAlt = false;

            for (WebElement image : images) {
                String alt = image.getAttribute("alt");
                String src = image.getAttribute("src");

                if (alt == null || alt.isEmpty()) {
                    if (!foundImageWithoutAlt) {
                        writerWithoutAlt.write("\n"+ "Page URL: " + url + "\n");
                        foundImageWithoutAlt = true;
                    }
                    writerWithoutAlt.write("" + src + "\n");
                } else {
                    if (!foundImageWithAlt) {
                        writerWithAlt.write("\n"+ "Page URL: " + url + "\n");
                        foundImageWithAlt = true;
                    }
                    writerWithAlt.write("Image with alt attribute found: " + src + " (alt: " + alt + ")\n");
                }
            }
        }

        // Close the reader and writers
        reader.close();
        writerWithAlt.close();
        writerWithoutAlt.close();

    
	}
}













































*/




/*

@Test



//Basic Alt tag List 
public void ALTTagchecker() throws FileNotFoundException, IOException, InterruptedException {

	WebDriverManager.firefoxdriver().setup();
	WebDriver driver = new FirefoxDriver();

	String filePath = "D:\\Ashoksinh Data\\My Desktop\\Opportunity Austin\\Alt.txt";

	// Read page URLs from the text file
	try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
		String line;

		while ((line = reader.readLine()) != null) {

			driver.get(line);

			Thread.sleep(2000);

			// Find all image elements on the page
			java.util.List<WebElement> images = driver.findElements(By.tagName("img"));

			// Check the "alt" attribute for each image
			for (WebElement image : images) {
				String altAttribute = image.getAttribute("alt");
				String srcAttribute = image.getAttribute("src");

				if (altAttribute == null || altAttribute.isEmpty()) {
					System.out.println("Image with no alt attribute found: " + srcAttribute);

				} else {

					System.out.println(
							"Image with alt attribute '" + altAttribute + "' found: " + "\n" + srcAttribute);
				}
			}
		}

		driver.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
}

}

*/

/*
// Without pages URL list of all alt tag images URL:
public static void main(String[] args) throws IOException, InterruptedException {

	WebDriverManager.firefoxdriver().setup();
    WebDriver driver = new FirefoxDriver();

    String filePath = "D:\\Ashoksinh Data\\My Desktop\\Alaska MHTA\\alt.txt";

    // Read page URLs from the text file
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;

        // Create BufferedWriter objects for writing data into separate text files
        BufferedWriter writerWithAlt = new BufferedWriter(new FileWriter("withAlt.txt"));
        BufferedWriter writerWithoutAlt = new BufferedWriter(new FileWriter("withoutAlt.txt"));

        while ((line = reader.readLine()) != null) {

            driver.get(line);

            Thread.sleep(2000);

            // Find all image elements on the page
            List<WebElement> images = driver.findElements(By.tagName("img"));

            // Check the "alt" attribute for each image
            
            for (WebElement image : images) {
            	
                String altAttribute = image.getAttribute("alt");
                String srcAttribute = image.getAttribute("src");

                if (altAttribute == null || altAttribute.isEmpty()) {
                    System.out.println("Image with no alt attribute found: " + srcAttribute);
                    writerWithoutAlt.write("Image with no alt attribute found: " + srcAttribute);
                    writerWithoutAlt.newLine();

                } else {
                    System.out.println(
                            "Image with alt attribute '" + altAttribute + "' found: " + "\n" + srcAttribute);
                    writerWithAlt.write("Image with alt attribute '" + altAttribute + "' found: " + "\n" + srcAttribute);
                    writerWithAlt.newLine();
                }
            }
        }

        // Close BufferedWriter objects
        writerWithAlt.close();
        writerWithoutAlt.close();
        
        driver.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
    
    
    */
    
    
    
    ///With Page URL list page Images //
