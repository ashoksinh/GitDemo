package my_projects;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TeamDataScraper {

    WebDriver driver;
    Workbook workbook;
    Sheet sheet;

    @BeforeTest
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Team Data");

        // Set header row in the Excel sheet
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Name");
        headerRow.createCell(1).setCellValue("Email");
        headerRow.createCell(2).setCellValue("Title");
        headerRow.createCell(3).setCellValue("LinkedIn URL");
        headerRow.createCell(4).setCellValue("Verification Status");
    }

    @Test
    public void fetchAndVerifyTeamData() throws IOException {
        // Fetch data from the website
        fetchTeamData();

        // Verify data with client sheet
        verifyWithClientData("D:\\All Data 2024\\My Desktop\\Encore Funding\\13-12-2024\\New From client side QA final 20-12-2024 .xlsx");

        // Write data to Excel file
        try (FileOutputStream fileOut = new FileOutputStream("TeamDataVerified.xlsx")) {
            workbook.write(fileOut);
        }
    }

    private void fetchTeamData() {
        driver.get("https://encore-funding.com/about-us/meet-the-team-copy/?5345");

        // Locate all team member cards
        List<WebElement> teamCards = driver.findElements(By.xpath("//div[@class='ourTeamBoxWrapper w-25']"));
        int rowIndex = 1;

        for (WebElement card : teamCards) {
            Row row = sheet.createRow(rowIndex++);

            // Extract Name
            String name = card.findElement(By.xpath(".//h5")).getText(); // Relative XPath to h5 inside the current card
            row.createCell(0).setCellValue(name);

            // Extract Title
            String title = card.findElement(By.xpath(".//h6")).getText(); // Relative XPath to h6 inside the current card
            row.createCell(2).setCellValue(title);

            // Extract LinkedIn URL (if present)
            try {
                WebElement linkedInIcon = card.findElement(By.xpath(".//div[contains(@class, 'socialLinkWrapper')]/a"));
                String linkedInUrl = linkedInIcon.getAttribute("href");
                row.createCell(3).setCellValue(linkedInUrl);
            } catch (Exception e) {
                row.createCell(3).setCellValue("N/A"); // If LinkedIn URL is not available
            }

            // Extract Email (if present)
            try {
                WebElement emailIcon = card.findElement(By.xpath(".//i[contains(@class, 'fa-envelope-open')]/parent::a"));
                String email = emailIcon.getAttribute("href").replace("mailto:", "");
                row.createCell(1).setCellValue(email);
            } catch (Exception e) {
                row.createCell(1).setCellValue("N/A"); // If email is not available
            }
        }
    }

    private void verifyWithClientData(String clientFilePath) throws IOException {
        // Load client Excel sheet
        try (FileInputStream clientFile = new FileInputStream(clientFilePath)) {
            Workbook clientWorkbook = new XSSFWorkbook(clientFile);
            Sheet clientSheet = clientWorkbook.getSheetAt(0);

            // Create a set of client names for quick lookup
            Set<String> clientNames = new HashSet<>();
            for (int i = 1; i <= clientSheet.getLastRowNum(); i++) {
                Row row = clientSheet.getRow(i);
                if (row != null) {
                    String clientName = getCellValue(row, 0); // Assuming "Name" is in the first column
                    clientNames.add(clientName.trim().toLowerCase());
                }
            }

            // Verify names in the generated sheet
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    String name = getCellValue(row, 0);
                    if (clientNames.contains(name.trim().toLowerCase())) {
                        row.createCell(4).setCellValue("Match");
                    } else {
                        row.createCell(4).setCellValue("No Match");
                    }
                }
            }
        }
    }

    private String getCellValue(Row row, int column) {
        Cell cell = row.getCell(column);
        return cell != null ? cell.toString().trim() : "";
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}





















/*
package my_projects;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class TeamDataScraper {

    WebDriver driver;
    Workbook workbook;
    Sheet sheet;

    @BeforeTest
    public void setUp() {
       
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Team Data");

        // Set header row in the Excel sheet
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Name");
        headerRow.createCell(1).setCellValue("Email");
        headerRow.createCell(2).setCellValue("Title");
        headerRow.createCell(3).setCellValue("LinkedIn URL");
    }

    @Test
    public void fetchTeamData() throws IOException {
        driver.get("https://encore-funding.com/about-us/meet-the-team-copy/?5345");

        // Locate all team member cards
        List<WebElement> teamCards = driver.findElements(By.xpath("//div[@class='ourTeamBoxWrapper w-25']"));

        int rowIndex = 1;

        for (WebElement card : teamCards) {
            Row row = sheet.createRow(rowIndex++);

            // Extract Name
            String name = card.findElement(By.xpath(".//h5")).getText(); // Relative XPath to h5 inside the current card
            row.createCell(0).setCellValue(name);

            // Extract Title
            String title = card.findElement(By.xpath(".//h6")).getText(); // Relative XPath to h6 inside the current card
            row.createCell(2).setCellValue(title);

            // Extract LinkedIn URL (if present)
            try {
                WebElement linkedInIcon = card.findElement(By.xpath(".//div[contains(@class, 'socialLinkWrapper')]/a"));
                String linkedInUrl = linkedInIcon.getAttribute("href");
                row.createCell(3).setCellValue(linkedInUrl);
            } catch (Exception e) {
                row.createCell(3).setCellValue("N/A"); // If LinkedIn URL is not available
            }

            // Extract Email (if present)
            try {
                WebElement emailIcon = card.findElement(By.xpath(".//i[contains(@class, 'fa-envelope-open')]/parent::a"));
                String email = emailIcon.getAttribute("href").replace("mailto:", "");
                row.createCell(1).setCellValue(email);
            } catch (Exception e) {
                row.createCell(1).setCellValue("N/A"); // If email is not available
            }
        }

        // Write data to Excel file
        try (FileOutputStream fileOut = new FileOutputStream("TeamData.xlsx")) {
            workbook.write(fileOut);
        }
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
*/
