package New_Code;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.bonigarcia.wdm.WebDriverManager;

public class LionWordChecker {

    @Test
    public void ReplaceWordFinder() throws InterruptedException, IOException {
        WebDriver driver = null;
        FileInputStream fis = null;
        Workbook workbook = null;

        try {
            // Set up WebDriver (ChromeDriver)
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            // Load Excel file
            fis = new FileInputStream("C:\\Users\\ashoksinh\\Desktop\\Heidenhein\\1.xlsx");
            workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0); // Assuming URLs are in the first sheet
            fis.close(); // Close FileInputStream after loading workbook

            // Loop through each row in the Excel sheet
            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell urlCell = row.getCell(0); // Assuming URL is in the first column
                    Cell resultCell = row.createCell(1); // Result column

                    String url = urlCell.getStringCellValue();
                    System.out.println("Processing URL: " + url);

                    // Open the URL
                    driver.get(url);

                    // Wait for the page body to fully load
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

                    // Get the body text of the page
                    WebElement bodyElement = driver.findElement(By.tagName("body"));
                    String bodyText = bodyElement.getText().toLowerCase();

                    // Perform verification
                    boolean tnc7Found = bodyText.contains("tnc7");
                    boolean tnc7BasicFound = bodyText.contains("tnc7 basic");

                    // Use XPath to validate "Dplus" formatting
                    boolean dplusFoundItalicized = bodyText.contains("D<em>plus</em>");

                    boolean apostropheRemoved = !bodyText.contains("heidenhain's") && bodyText.contains("heidenhain");

                    // Check Breadcrumb Update
                    boolean breadcrumbUpdated = false;
                    List<WebElement> breadcrumbs = driver.findElements(By.xpath("//nav[contains(@class, 'breadcrumb')]"));
                    if (!breadcrumbs.isEmpty()) {
                        breadcrumbUpdated = breadcrumbs.stream()
                                .anyMatch(breadcrumb -> breadcrumb.getText().contains("Dplus"));
                    }

                    // Check Slider Update
                    boolean sliderUpdated = false;
                    List<WebElement> sliders = driver.findElements(By.xpath("//div[contains(@class, 'slider') and contains(text(),'HEIDENHAIN Dplus encoders')]"));
                    if (!sliders.isEmpty()) {
                        sliderUpdated = sliders.size() > 0;
                    }

                    // Validate and Log Results
                    if (tnc7Found && !tnc7BasicFound) {
                        resultCell.setCellValue("TNC7 not replaced with TNC7 basic.");
                    } else if (dplusFoundItalicized) {
                        resultCell.setCellValue("Dplus is correctly italicized as 'plus'.");
                    } else if (!apostropheRemoved) {
                        resultCell.setCellValue("Apostrophe in HEIDENHAIN not removed.");
                    } else if (!breadcrumbs.isEmpty() && !breadcrumbUpdated) {
                        resultCell.setCellValue("Breadcrumb text mismatched or not updated to 'Dplus'.");
                    } else if (!sliders.isEmpty() && !sliderUpdated) {
                        resultCell.setCellValue("Slider text mismatched or not updated to 'HEIDENHAIN Dplus encoders'.");
                    } else {
                        resultCell.setCellValue("All checks passed.");
                    }


                    // Write the result back to the Excel file
                    try (FileOutputStream fos = new FileOutputStream("C:\\Users\\ashoksinh\\Desktop\\Heidenhein\\1.xlsx")) {
                        workbook.write(fos);
                    } catch (IOException e) {
                        System.err.println("Failed to write to Excel: " + e.getMessage());
                    }

                    System.out.println("Result written for URL: " + url);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources
            if (workbook != null) workbook.close();
            if (driver != null) driver.quit();
        }
    }
}








/*

package New_Code;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import io.github.bonigarcia.wdm.WebDriverManager;

public class LionWordChecker {

    @Test
    public void ReplaceWordFinder() throws InterruptedException, IOException {
        WebDriver driver = null;
        FileInputStream fis = null;
        Workbook workbook = null;

        try {
            // Set up WebDriver (ChromeDriver)
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            // Load Excel file
            fis = new FileInputStream("C:\\Users\\ashoksinh\\Desktop\\Heidenhein\\1.xlsx");
            workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0); // Assuming URLs are in the first sheet
            fis.close(); // Close FileInputStream after loading workbook

            // Loop through each row in the Excel sheet
            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell urlCell = row.getCell(0); // Assuming URL is in the first column
                    Cell resultCell = row.createCell(1); // Result column

                    String url = urlCell.getStringCellValue();
                    System.out.println("Processing URL: " + url);

                    // Open the URL
                    driver.get(url);

                    // Wait for the page body to fully load
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

                    // Get the body text of the page
                    WebElement bodyElement = driver.findElement(By.tagName("body"));
                    String bodyText = bodyElement.getText().toLowerCase();

                    // Perform verification
                    boolean tnc7Found = bodyText.contains("tnc7");
                    boolean tnc7BasicFound = bodyText.contains("tnc7 basic");
                    boolean dplusFoundItalicized = bodyText.contains("Dp<em>lus</em>");
                   boolean apostropheRemoved = !bodyText.contains("heidenhain's") && bodyText.contains("heidenhain");
                    boolean breadcrumbUpdated = driver.findElements(By.xpath("//nav[contains(@class, 'breadcrumb')]")).stream()
                            .anyMatch(breadcrumb -> breadcrumb.getText().contains("Dplus"));
                    boolean sliderUpdated = driver.findElements(By.xpath("//div[contains(@class, 'slider') and contains(text(),'HEIDENHAIN Dplus encoders')]")).size() > 0;

                    // Validate results
                    if (tnc7Found && !tnc7BasicFound) {
                        resultCell.setCellValue("TNC7 not replaced with TNC7 basic.");
                    } else if (dplusFoundItalicized) {
                        resultCell.setCellValue("Dplus is correctly italicized as 'plus'.");
                    } else if (!apostropheRemoved) {
                        resultCell.setCellValue("Apostrophe in HEIDENHAIN not removed.");
                    } else if (!breadcrumbUpdated) {
                        resultCell.setCellValue("Breadcrumb not updated from 'plus' to 'Dplus'.");
                    } else if (!sliderUpdated) {
                        resultCell.setCellValue("Slider element not updated to 'HEIDENHAIN Dplus encoders'.");
                    } else {
                        resultCell.setCellValue("All checks passed.");
                    }

                    // Write the result back to the Excel file
                    try (FileOutputStream fos = new FileOutputStream("C:\\Users\\ashoksinh\\Desktop\\Heidenhein\\1.xlsx")) {
                        workbook.write(fos);
                    } catch (IOException e) {
                        System.err.println("Failed to write to Excel: " + e.getMessage());
                    }

                    System.out.println("Result written for URL: " + url);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources
            if (workbook != null) workbook.close();
            if (driver != null) driver.quit();
        }
    }
}

*/
