package my_projects;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Register {

	@Test
	public void f() throws FileNotFoundException, IOException, InterruptedException

	{
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		
		driver.get("https://www.havis.com/my-account/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		
		/*
		WebElement Close = driver.findElement(By.xpath("/html/body/div[11]/div/a/i"));
		js.executeScript("arguments[0].setAttribute('style', 'border:2px solid red; background:green')", Close);
		Close.click();
	*/
	
		WebElement RegisterNow = driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[1]/div[1]/div/form/p[5]/a\n"));
		js.executeScript("arguments[0].setAttribute('style', 'border:2px solid red; background:green')", RegisterNow);
		RegisterNow.click();
			
		WebElement firstname=driver.findElement(By.xpath("//*[@id=\"input_2_1\"]\n"));
		firstname.sendKeys("Jarves");
		Thread.sleep(2000);
		
		WebElement lastname=driver.findElement(By.xpath("//*[@id=\"input_2_4\"]\n"));
		lastname.sendKeys("Johnson");
		Thread.sleep(2000);
		
		WebElement CompanyName=driver.findElement(By.xpath("//*[@id=\"input_2_5\"]\n"));
		CompanyName.sendKeys("Johnson & Sons LLC");
		Thread.sleep(2000);
		
		WebElement Title=driver.findElement(By.xpath("//*[@id=\"input_2_6\"]"));
		Title.sendKeys("SEO Manager");
		Thread.sleep(2000);		
		
		WebElement Address1=driver.findElement(By.xpath("//*[@id=\"input_2_9\"]"));
		Address1.sendKeys("456 Oak Street");
		Thread.sleep(2000);
		
		WebElement City=driver.findElement(By.xpath("//*[@id=\"input_2_17\"]\n"));
		City.sendKeys("Springfield");
		Thread.sleep(2000);
		
		WebElement Postal=driver.findElement(By.xpath("//*[@id=\"input_2_18\"]"));
		Postal.sendKeys("54321");
		Thread.sleep(2000);
		
		WebElement Phone=driver.findElement(By.xpath("//*[@id=\"input_2_19\"]"));
		Phone.sendKeys("559-987-6543");
		Thread.sleep(5000);
		
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Phone);
		
		WebElement Country = driver.findElement(By.id("input_2_20"));
		org.openqa.selenium.support.ui.Select dropdowns = new org.openqa.selenium.support.ui.Select(Country); 
		Thread.sleep(7000);
		dropdowns.selectByVisibleText("United States");

		
		WebElement State = driver.findElement(By.id("input_2_21"));
		org.openqa.selenium.support.ui.Select dropdowns1 = new org.openqa.selenium.support.ui.Select(State);  
		Thread.sleep(4000);
		dropdowns1.selectByVisibleText("New York");
		//System.out.println(dropdowns.getFirstSelectedOption().getText());	

		WebElement Checkobx=driver.findElement(By.xpath("//*[@id=\"choice_2_22_1\"]"));
		Checkobx.click();
		Thread.sleep(2000);
		
		WebElement Username=driver.findElement(By.xpath("//*[@id=\"input_2_14\"]"));
		Username.sendKeys("MichaelSmith001");
		Thread.sleep(2000);
		
		WebElement Email=driver.findElement(By.xpath("//*[@id=\"input_2_12\"]"));
		Email.sendKeys("jarves@boranora.com");
		Thread.sleep(2000);
				
		WebElement Password=driver.findElement(By.xpath("//*[@id=\"input_2_15\"]\n"));
		Password.sendKeys("wW4%eJ3)oW3\"nX8{aD8@");
		Thread.sleep(2000);
		
		WebElement ConfirmPassword=driver.findElement(By.xpath("//*[@id=\"input_2_15_2\"]\n"));
		ConfirmPassword.sendKeys("wW4%eJ3)oW3\"nX8{aD8@");
		Thread.sleep(2000);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ConfirmPassword);
		
		WebElement Category = driver.findElement(By.id("input_2_13"));
		org.openqa.selenium.support.ui.Select dropdowns3 = new org.openqa.selenium.support.ui.Select(Category); 
		Thread.sleep(5000);
		dropdowns3.selectByVisibleText("I would like to discuss affiliation opportunities (Affiliate)");
		
		WebElement Market = driver.findElement(By.id("input_2_23"));
		org.openqa.selenium.support.ui.Select dropdowns4 = new org.openqa.selenium.support.ui.Select(Market);  
		Thread.sleep(4000);
		dropdowns4.selectByVisibleText("Public Safety");

		WebElement Aboutus = driver.findElement(By.id("input_2_24"));
		org.openqa.selenium.support.ui.Select dropdowns5 = new org.openqa.selenium.support.ui.Select(Aboutus);  
		Thread.sleep(4000);
		dropdowns5.selectByVisibleText("Search Engine");
		//((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Aboutus);
		
		WebElement Comments=driver.findElement(By.xpath("//*[@id=\"input_2_25\"]\n"));
		Comments.sendKeys("This is a notification to inform you that a comment form submission has been made for testing purposes. Please note that this submission is part of our testing procedures and does not require any action from your end.");
		Thread.sleep(4000);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",Aboutus);
		
		WebElement PolicyCheckbox=driver.findElement(By.xpath("//*[@id=\"choice_2_26_1\"]"));
		PolicyCheckbox.click();
		Thread.sleep(3000);
		
		/*
		
		WebElement Register=driver.findElement(By.xpath("//*[@id=\"gform_submit_button_2\"]\n"));
		Register.click();
		Thread.sleep(7000);
		
		driver.close();
		*/	
		
		

		
	}
}
