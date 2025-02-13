package my_projects;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Register_Primal {

	WebDriver driver;

	@BeforeMethod
	public void setUp() {
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	}

	@Test
	public void testRegisterUser() throws InterruptedException {
		// Navigate to registration page
		driver.get("https://primalbod.wpenginepowered.com/my-account/");

		// Click the "Create an Account" button
		driver.findElement(By.cssSelector("p.gotoregister")).click();

		// Fill in the registration form
		driver.findElement(By.xpath("//input[@id='input_2_1_3']")).sendKeys("dev");
		driver.findElement(By.xpath("//input[@id='input_2_1_6']")).sendKeys("Shah");

		driver.findElement(By.xpath("//input[@placeholder='Phone']")).sendKeys("+135458568");

		driver.findElement(By.xpath("//input[@name='input_7']"))
				.sendKeys("D:\\Download folder data\\ashoksinhzala.ovpn");

		// driver.findElement(By.xpath("//input[@name='input_7']")).sendKeys("D:\\Download
		// folder data\\002LEA_1.MP4");

		driver.findElement(By.xpath("//input[@id='input_2_8']")).sendKeys("vika1972");
		driver.findElement(By.xpath("//input[@id='input_2_9']")).sendKeys("vika1972nik@maxseeding.com");

		driver.findElement(By.xpath("//input[@id='input_2_4']")).sendKeys("iF4~sM3}eE4(nK7(");
		driver.findElement(By.xpath("//input[@id='input_2_4_2']")).sendKeys("iF4~sM3}eE4(nK7(");


		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,850)", "");

		Thread.sleep(3000);
		driver.findElement(By.xpath("//input[@id='gform_submit_button_2']")).click();

		String successMessage = driver.findElement(By.cssSelector("h2.gform_submission_error")).getText();
		Assert.assertTrue(
				successMessage.contains("There wQas a problem with your submission. Please review the fields below."));

		
	}

	@AfterMethod
	public void tearDown() {

		// driver.close();
	}
}