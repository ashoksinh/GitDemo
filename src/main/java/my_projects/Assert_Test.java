package my_projects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Assert_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		
		driver.manage().window().maximize();
		
		driver.get("https://ultimateqa.com/simple-html-elements-for-automation/");
		
		Assert.assertFalse(driver.findElement(By.cssSelector("input[value='Bike']")).isSelected());
		
		System.out.println(driver.findElement(By.cssSelector("input[value='Bike']")).isSelected());
		
	driver.findElement(By.cssSelector("input[value='Bike']")).click();
//		
//		System.out.println(driver.findElement(By.cssSelector("input[value='Bike']")).isSelected());
//		
	Assert.assertTrue(driver.findElement(By.cssSelector("input[value='Bike']")).isSelected());
		
		
	}
}
