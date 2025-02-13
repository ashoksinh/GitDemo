package my_projects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class One_QA {
	@Test

	public void FindH1Tags() throws InterruptedException {

		WebDriverManager.firefoxdriver().setup();
		WebDriver driver = new FirefoxDriver();
		driver.get("https://www.havis.com/");
		driver.manage().window().maximize();
		Thread.sleep(5000);

		java.util.List<WebElement> h1Tags = driver.findElements(By.tagName("h1"));
		
		for(WebElement h1Tag: h1Tags)
		{
			System.out.println("List of H1 Tags:\t" +h1Tag.getText());
						
		}
		
		driver.close();
	}
}