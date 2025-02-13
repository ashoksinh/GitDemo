package Load_test;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AdvancedLoadTest {

    private static final int NUM_USERS = 10;
    private static final int THREAD_TIMEOUT_SECONDS = 120;

    private static List<Long> responseTimes = Collections.synchronizedList(new ArrayList<>());
    private static List<Long> networkSent = Collections.synchronizedList(new ArrayList<>());
    private static List<Long> networkReceived = Collections.synchronizedList(new ArrayList<>());
    private static List<LocalDateTime> localDateTimes = Collections.synchronizedList(new ArrayList<>());

    @BeforeClass
    public void setup() {
        // Setup code, if needed, before starting tests
    }

    @Test
    public void performLoadTest() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_USERS);

        for (int i = 0; i < NUM_USERS; i++) {
            executorService.submit(() -> {
                WebDriver driver = null;
                BrowserMobProxy proxy = null;
                try {
                    // Start BrowserMob Proxy for this thread
                    proxy = new BrowserMobProxyServer();
                    proxy.setTrustAllServers(true);
                    proxy.start(0);
                    Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

                    // Configure ChromeDriver
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--ignore-certificate-errors");
                    options.addArguments("--allow-insecure-localhost");
                    options.setProxy(seleniumProxy);

                    driver = new ChromeDriver(options);

                    // Start capturing HAR
                    proxy.newHar("test");

                    // Measure response time
                    long startTimeMillis = System.currentTimeMillis();
                    driver.get("https://www.google.com/"); // Target URL
                    TimeUnit.SECONDS.sleep(2); // Simulate user delay
                    long endTimeMillis = System.currentTimeMillis();
                    responseTimes.add(endTimeMillis - startTimeMillis);

                    // Capture network metrics
                    Har har = proxy.getHar();
                    long totalSent = har.getLog().getEntries().stream().mapToLong(entry -> entry.getRequest().getBodySize()).sum();
                    long totalReceived = har.getLog().getEntries().stream().mapToLong(entry -> entry.getResponse().getBodySize()).sum();

                    networkSent.add(totalSent);
                    networkReceived.add(totalReceived);

                    localDateTimes.add(LocalDateTime.now());
                } catch (Exception e) {
                    Assert.fail("Test failed due to an exception: " + e.getMessage());
                } finally {
                    if (driver != null) {
                        driver.quit();
                    }
                    if (proxy != null) {
                        proxy.stop();
                    }
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(THREAD_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    @AfterClass
    public void tearDown() {
        generateHtmlReport();
    }

    private void generateHtmlReport() {
        try {
            File file = new File("Load_performance_QA_report.html");
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("<html>");
            bw.write("<head>");
            bw.write("<title>Enhanced Load Performance Test Report</title>");
            bw.write("<style>");
            bw.write("body { font-family: Arial, sans-serif; background-color: #f4f4f9; color: #333; }");
            bw.write("h1 { color: #4CAF50; text-align: center; }");
            bw.write("table { width: 80%; margin: 20px auto; border-collapse: collapse; background-color: #fff; }");
            bw.write("th, td { padding: 10px; border: 1px solid #ddd; text-align: center; }");
            bw.write("th { background-color: #4CAF50; color: white; }");
            bw.write("tr:nth-child(even) { background-color: #f2f2f2; }");
            bw.write("</style>");
            bw.write("</head>");
            bw.write("<body>");
            bw.write("<h1>Enhanced Load Performance Test Report</h1>");

            if (responseTimes.isEmpty()) {
                bw.write("<p style='text-align: center;'>No valid data was recorded during the test.</p>");
            } else {
                bw.write("<h2 style='text-align: center;'>Detailed Results</h2>");
                bw.write("<table>");
                bw.write("<tr><th>User</th><th>Response Time (ms)</th><th>Network Sent (bytes)</th><th>Network Received (bytes)</th><th>LocalDateTime</th></tr>");
                for (int i = 0; i < responseTimes.size(); i++) {
                    bw.write("<tr><td>" + (i + 1) + "</td><td>" + responseTimes.get(i) + "</td><td>"
                            + networkSent.get(i) + "</td><td>" + networkReceived.get(i) + "</td><td>"
                            + localDateTimes.get(i).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "</td></tr>");
                }
                bw.write("</table>");
            }

            bw.write("</body>");
            bw.write("</html>");

            bw.close();
            fw.close();

            System.out.println("Enhanced report generated: " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}






















/*
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AdvancedLoadTest {

    private static final int NUM_USERS = 10;
    private static final int RAMP_UP_TIME_SECONDS = 10;
    private static final int TEST_DURATION_SECONDS = 120; // Increased test duration

    private static List<Long> responseTimes = Collections.synchronizedList(new ArrayList<>());
    private static List<Long> networkSent = Collections.synchronizedList(new ArrayList<>());
    private static List<Long> networkReceived = Collections.synchronizedList(new ArrayList<>());
    private static List<LocalDateTime> localDateTimes = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(NUM_USERS);

        LocalDateTime startTime = LocalDateTime.now();

        for (int i = 0; i < NUM_USERS; i++) {
            executor.submit(() -> {
                WebDriver driver = null;
                BrowserMobProxy proxy = null;
                try {
                    // Start BrowserMob Proxy
                    proxy = new BrowserMobProxyServer();
                    proxy.setTrustAllServers(true);
                    proxy.start(0);
                    Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

                    // Configure ChromeDriver with proxy and SSL bypass options
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--ignore-certificate-errors");
                    options.addArguments("--allow-insecure-localhost");
                    options.setProxy(seleniumProxy);
                    driver = new ChromeDriver(options);
                 
                    // Start capturing HAR
                    proxy.newHar("test");

                    // Measure response time
                    long startTimeMillis = System.currentTimeMillis();
                    driver.get("https://rahulshettyacademy.com/");
                    long endTimeMillis = System.currentTimeMillis();
                    responseTimes.add(endTimeMillis - startTimeMillis);

                    // Simulate user actions
                    Actions actions = new Actions(driver);

                    // Scroll down the page
                    actions.moveToElement(driver.findElement(By.tagName("body"))).scrollByAmount(0, 500).perform();
                    TimeUnit.SECONDS.sleep(2);

                    // Click on a visible element (if available)
                    List<WebElement> clickableElements = driver.findElements(By.cssSelector("a"));
                    if (!clickableElements.isEmpty()) {
                        clickableElements.get(0).click();
                        TimeUnit.SECONDS.sleep(2);
                    }

                    // Capture network metrics
                    Har har = proxy.getHar();
                    long totalSent = har.getLog().getEntries().stream()
                            .mapToLong(entry -> entry.getRequest().getBodySize())
                            .sum();
                    long totalReceived = har.getLog().getEntries().stream()
                            .mapToLong(entry -> entry.getResponse().getBodySize())
                            .sum();

                    networkSent.add(totalSent);
                    networkReceived.add(totalReceived);

                    // Record local date and time
                    localDateTimes.add(LocalDateTime.now());

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (driver != null) {
                        driver.quit();
                    }
                    if (proxy != null) {
                        proxy.stop();
                    }
                }
            });

            try {
                TimeUnit.SECONDS.sleep(RAMP_UP_TIME_SECONDS / NUM_USERS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        try {
            executor.awaitTermination(TEST_DURATION_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LocalDateTime endTime = LocalDateTime.now();

        generateHtmlReport(startTime, endTime, responseTimes, networkSent, networkReceived, localDateTimes);
    }

    private static void generateHtmlReport(LocalDateTime startTime, LocalDateTime endTime, List<Long> responseTimes, List<Long> networkSent, List<Long> networkReceived, List<LocalDateTime> localDateTimes) {
        try {
            File file = new File("Load_performance_QA_report.html");
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("<html>");
            bw.write("<head>");
            bw.write("<title>Enhanced Load Performance Test Report</title>");
            bw.write("<style>");
            bw.write("body { font-family: Arial, sans-serif; background-color: #f4f4f9; color: #333; }");
            bw.write("h1 { color: #4CAF50; text-align: center; }");
            bw.write("table { width: 80%; margin: 20px auto; border-collapse: collapse; background-color: #fff; }");
            bw.write("th, td { padding: 10px; border: 1px solid #ddd; text-align: center; }");
            bw.write("th { background-color: #4CAF50; color: white; }");
            bw.write("tr:nth-child(even) { background-color: #f2f2f2; }");
            bw.write("</style>");
            bw.write("</head>");
            bw.write("<body>");
            bw.write("<h1>Enhanced Load Performance Test Report</h1>");

            bw.write("<h2 style='text-align: center;'>Test Summary</h2>");
            bw.write("<p style='text-align: center;'>Start Time: " + startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "</p>");
            bw.write("<p style='text-align: center;'>End Time: " + endTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "</p>");
            bw.write("<p style='text-align: center;'>Number of Users: " + NUM_USERS + "</p>");
            bw.write("<p style='text-align: center;'>Ramp-Up Time: " + RAMP_UP_TIME_SECONDS + " seconds</p>");
            bw.write("<p style='text-align: center;'>Test Duration: " + TEST_DURATION_SECONDS + " seconds</p>");

            if (responseTimes.isEmpty()) {
                bw.write("<p style='text-align: center;'>No valid data was recorded during the test.</p>");
            } else {
                bw.write("<h2 style='text-align: center;'>Detailed Results</h2>");
                bw.write("<table>");
                bw.write("<tr><th>User</th><th>Response Time (ms)</th><th>Network Sent (bytes)</th><th>Network Received (bytes)</th><th>LocalDateTime</th></tr>");
                for (int i = 0; i < responseTimes.size(); i++) {
                    bw.write("<tr><td>" + (i + 1) + "</td><td>" + responseTimes.get(i) + "</td><td>" + networkSent.get(i) + "</td><td>" + networkReceived.get(i) + "</td><td>" + localDateTimes.get(i).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "</td></tr>");
                }
                bw.write("</table>");
            }

            bw.write("</body>");
            bw.write("</html>");

            bw.close();
            fw.close();

            System.out.println("Enhanced report generated: " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



*/


















/*
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AdvancedLoadTest {

    private static final int NUM_USERS = 10;
    private static final int RAMP_UP_TIME_SECONDS = 10;

    private static List<Long> responseTimes = Collections.synchronizedList(new ArrayList<>());
    private static List<Long> networkSent = Collections.synchronizedList(new ArrayList<>());
    private static List<Long> networkReceived = Collections.synchronizedList(new ArrayList<>());
    private static List<LocalDateTime> localDateTimes = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(NUM_USERS);

        LocalDateTime startTime = LocalDateTime.now();

        for (int i = 0; i < NUM_USERS; i++) {
            executor.submit(() -> {
                WebDriver driver = null;
                BrowserMobProxy proxy = null;
                try {
                    proxy = new BrowserMobProxyServer();
                    proxy.setTrustAllServers(true);
                    proxy.start(0);
                    Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--ignore-certificate-errors");
                    options.addArguments("--allow-insecure-localhost");
                    options.setProxy(seleniumProxy);
                    driver = new ChromeDriver(options);
                    driver.manage().window().maximize();

                    proxy.newHar("test");

                    long startTimeMillis = System.currentTimeMillis();
                    driver.get("https://rahulshettyacademy.com/");
                    long endTimeMillis = System.currentTimeMillis();
                    responseTimes.add(endTimeMillis - startTimeMillis);

                    Har har = proxy.getHar();
                    long totalSent = har.getLog().getEntries().stream()
                            .mapToLong(entry -> entry.getRequest().getBodySize())
                            .sum();
                    long totalReceived = har.getLog().getEntries().stream()
                            .mapToLong(entry -> entry.getResponse().getBodySize())
                            .sum();

                    networkSent.add(totalSent);
                    networkReceived.add(totalReceived);

                    localDateTimes.add(LocalDateTime.now());

                    TimeUnit.SECONDS.sleep(5);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (driver != null) {
                        driver.quit();
                    }
                    if (proxy != null) {
                        proxy.stop();
                    }
                }
            });

            try {
                TimeUnit.SECONDS.sleep(RAMP_UP_TIME_SECONDS / NUM_USERS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LocalDateTime endTime = LocalDateTime.now();

        generateHtmlReport(startTime, endTime, responseTimes, networkSent, networkReceived, localDateTimes);
    }

    private static void generateHtmlReport(LocalDateTime startTime, LocalDateTime endTime, List<Long> responseTimes, List<Long> networkSent, List<Long> networkReceived, List<LocalDateTime> localDateTimes) {
        try {
            File file = new File("load_performance_report.html");
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("<html>");
            bw.write("<head>");
            bw.write("<title>Load Performance Test Report</title>");
            bw.write("<style>");
            bw.write("body { font-family: Arial, sans-serif; background-color: #f4f4f9; color: #333; }");
            bw.write("h1 { color: #4CAF50; text-align: center; }");
            bw.write("table { width: 80%; margin: 20px auto; border-collapse: collapse; background-color: #fff; }");
            bw.write("th, td { padding: 10px; border: 1px solid #ddd; text-align: center; }");
            bw.write("th { background-color: #4CAF50; color: white; }");
            bw.write("tr:nth-child(even) { background-color: #f2f2f2; }");
            bw.write("</style>");
            bw.write("</head>");
            bw.write("<body>");
            bw.write("<h1>Load Performance Test Report</h1>");

            bw.write("<h2 style='text-align: center;'>Test Summary</h2>");
            bw.write("<p style='text-align: center;'>Start Time: " + startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "</p>");
            bw.write("<p style='text-align: center;'>End Time: " + endTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "</p>");
            bw.write("<p style='text-align: center;'>Number of Users: " + NUM_USERS + "</p>");
            bw.write("<p style='text-align: center;'>Ramp-Up Time: " + RAMP_UP_TIME_SECONDS + " seconds</p>");

            if (responseTimes.isEmpty()) {
                bw.write("<p style='text-align: center;'>No valid data was recorded during the test.</p>");
            } else {
                bw.write("<h2 style='text-align: center;'>Detailed Results</h2>");
                bw.write("<table>");
                bw.write("<tr><th>User</th><th>Response Time (ms)</th><th>Network Sent (bytes)</th><th>Network Received (bytes)</th><th>LocalDateTime</th></tr>");
                for (int i = 0; i < responseTimes.size(); i++) {
                    bw.write("<tr><td>" + (i + 1) + "</td><td>" + responseTimes.get(i) + "</td><td>" + networkSent.get(i) + "</td><td>" + networkReceived.get(i) + "</td><td>" + localDateTimes.get(i).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "</td></tr>");
                }
                bw.write("</table>");
            }

            bw.write("</body>");
            bw.write("</html>");

            bw.close();
            fw.close();

            System.out.println("Styled report generated: " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
*/

