package my_projects;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

public class SiteComparisonTest {

    private WebDriver stageDriver; // Driver for the stage site
    private WebDriver comparisonDriver; // Driver for the comparison site
    private WebDriverWait stageWait;
    private WebDriverWait comparisonWait;
    private StringBuilder htmlReport;
    private final String comparisonSiteURL = "https://my2.singleplatform.com/accounts/login/";

    @BeforeClass
    public void setUp() {
      //  System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        this.stageDriver = new ChromeDriver();
        this.comparisonDriver = new ChromeDriver();
        this.stageWait = new WebDriverWait(stageDriver, Duration.ofSeconds(10));
        this.comparisonWait = new WebDriverWait(comparisonDriver, Duration.ofSeconds(10));

        // Initialize HTML report
        htmlReport = new StringBuilder();
        htmlReport.append("<html><head><style>table{width:100%;border-collapse:collapse;}table,th,td{border:1px solid black;}th,td{padding:8px;text-align:left;}tr:nth-child(even){background-color:#f2f2f2;}th{background-color:#4CAF50;color:white;}</style></head><body>");
        htmlReport.append("<table><tr><th>Stage Location Name</th><th>Stage Hours Detail</th><th>Stage Full Address</th><th>Live Site Location Name</th><th>Live Site Hours Detail</th><th>Live Site Full Address</th><th>Result</th></tr>");
    }

    @Test(dataProvider = "urlProvider")
    public void compareSites(String stageUrl) {
        // Step 1: Retrieve data from the current (stage) site
        stageDriver.get(stageUrl);

        // Wait until all stage data is fully retrieved
        String stageLocationName = getStageLocationName();
        String stageHoursDetail = getStageHoursDetail();
        String stageFullAddress = getStageFullAddress();

        // Only proceed to open comparison site if all stage data is available
        if (stageLocationName != null && stageHoursDetail != null && stageFullAddress != null) {
            // Step 2: Log in to the comparison site and search
            loginToComparisonSite("bencabo@bluecusa.com", "singleplatform1");

            // Enter city in the search bar on the comparison site and submit
            WebElement cityField = comparisonWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("id-city")));
            cityField.sendKeys(stageLocationName);

            WebElement submitButton = comparisonDriver.findElement(By.id("submit"));
            submitButton.click();

            // Step 3: Verify and retrieve the target data from the search results
            WebElement correctResult = selectCorrectSearchResult(stageLocationName);
            String liveSiteLocationName = "";
            String liveSiteHoursDetail = "";
            String liveSiteFullAddress = "";

            if (correctResult != null) {
                correctResult.click();
                switchToNewTab(comparisonDriver);

                liveSiteLocationName = getLiveSiteLocationName();
                liveSiteHoursDetail = getLiveSiteHoursDetail();
                liveSiteFullAddress = getLiveSiteFullAddress();

                comparisonDriver.close();
                switchToOriginalTab(comparisonDriver);
            } else {
                liveSiteLocationName = "Data Not Found";
                liveSiteHoursDetail = "Data Not Found";
                liveSiteFullAddress = "Data Not Found";
            }

            // Step 4: Add results to the HTML report
            htmlReport.append("<tr>");
            htmlReport.append("<td>").append(stageLocationName).append("</td>");
            htmlReport.append("<td>").append(stageHoursDetail).append("</td>");
            htmlReport.append("<td>").append(stageFullAddress).append("</td>");
            htmlReport.append("<td>").append(liveSiteLocationName).append("</td>");
            htmlReport.append("<td>").append(liveSiteHoursDetail).append("</td>");
            htmlReport.append("<td>").append(liveSiteFullAddress).append("</td>");
            htmlReport.append("<td>").append(compareData(stageLocationName, liveSiteLocationName) && compareData(stageHoursDetail, liveSiteHoursDetail) && compareData(stageFullAddress, liveSiteFullAddress)
                ? "&#10004;" : "&#10008;").append("</td>");
            htmlReport.append("</tr>");

            // TestNG Assertions to validate data comparison
            Assert.assertEquals(liveSiteLocationName, stageLocationName, "Location names do not match!");
            Assert.assertEquals(liveSiteHoursDetail, stageHoursDetail, "Hours details do not match!");
            Assert.assertEquals(liveSiteFullAddress, stageFullAddress, "Full addresses do not match!");
        } else {
            System.out.println("Data retrieval from the stage site failed for URL: " + stageUrl);
        }

        // Close comparison tab after each test run to avoid multiple open sessions
        comparisonDriver.get("about:blank");
    }

    // Log in to the comparison site with username and password
    private void loginToComparisonSite(String username, String password) {
        comparisonDriver.get(comparisonSiteURL);

        // Enter the username
        WebElement usernameField = comparisonWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("id_username")));
        usernameField.sendKeys(username);

        // Enter the password
        WebElement passwordField = comparisonDriver.findElement(By.id("id_password"));
        passwordField.sendKeys(password);

        // Click the login button
        WebElement loginButton = comparisonDriver.findElement(By.id("submit"));
        loginButton.click();

        // Wait for the dashboard or search page to load after login
        comparisonWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("id-city"))); // Adjust this to an element that appears after login
    }

    @AfterClass
    public void tearDown() throws IOException {
        htmlReport.append("</table></body></html>");
        FileUtils.writeStringToFile(new File("ComparisonReport.html"), htmlReport.toString(), "UTF-8");

        if (stageDriver != null) {
            stageDriver.quit();
        }
        if (comparisonDriver != null) {
            comparisonDriver.quit();
        }
    }

    @DataProvider(name = "urlProvider")
    public Object[][] urlProvider() throws IOException {
        List<String> urls = Files.readAllLines(Paths.get("D:\\All Data 2024\\My Desktop\\Black Bear Diner\\Automation File\\Location Pages URL.txt"));
        Object[][] data = new Object[urls.size()][1];
        for (int i = 0; i < urls.size(); i++) {
            data[i][0] = urls.get(i);
        }
        return data;
    }

    private boolean compareData(String stageData, String liveData) {
        return stageData != null && stageData.equalsIgnoreCase(liveData);
    }

    private String getStageLocationName() {
        return stageDriver.findElement(By.xpath("//div[contains(@class, 'container')]/h1")).getText(); // Adjust locator as needed
    }

    private String getStageHoursDetail() {
        return stageDriver.findElement(By.xpath("//div[@class='row word-break']/div")).getText(); // Adjust locator as needed
    }

    private String getStageFullAddress() {
        return stageDriver.findElement(By.xpath("//div[@class='location']/p[@class='spacing-30 mb0 h6 f26 fg-black80']")).getText(); // Locator for full address
    }

    private WebElement selectCorrectSearchResult(String stageLocationName) {
        List<WebElement> results = comparisonDriver.findElements(By.cssSelector(".result-class")); // Adjust selector
        for (WebElement result : results) {
            if (result.getText().contains(stageLocationName)) {
                return result;
            }
        }
        return null;
    }

    private String getLiveSiteLocationName() {
        return comparisonDriver.findElement(By.id("live-location-name-id")).getText(); // Adjust locator as needed
    }

    private String getLiveSiteHoursDetail() {
        return comparisonDriver.findElement(By.id("live-hours-detail-id")).getText(); // Adjust locator as needed
    }

    private String getLiveSiteFullAddress() {
        return comparisonDriver.findElement(By.id("live-full-address-id")).getText(); // Locator for full address on live site
    }

    private void switchToNewTab(WebDriver driver) {
        for (String windowHandle : driver.getWindowHandles()) {
            driver.switchTo().window(windowHandle);
        }
    }

    private void switchToOriginalTab(WebDriver driver) {
        driver.switchTo().window(driver.getWindowHandles().iterator().next());
    }
}
