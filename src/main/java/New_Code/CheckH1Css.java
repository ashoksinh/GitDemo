package New_Code;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


    // Helper method to read URLs from a file
    private List<String> loadUrlsFromFile(String fileName) {
        List<String> urlList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                urlList.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urlList;
    }

    // Helper method to write issues to a file
    private void writeIssuesToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String issue : issues) {
                writer.write(issue);
                writer.newLine();
            }
            System.out.println("Issues logged in " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

