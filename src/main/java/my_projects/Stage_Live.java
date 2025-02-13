package my_projects;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Stage_Live {

	public static void main(String[] args) throws IOException, InterruptedException {

		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();

//        String filePath = "D:\\Ashoksinh Data\\My Desktop\\RPS\\Alt tag .txt";
//     
//        
		List<String> foundPages = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(
				new FileReader("D:\\Ashoksinh Data\\My Desktop\\Carlise IT\\Carlise-All.txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				driver.get(line);

				// Get the page source
				String pageSource = driver.getPageSource();

				// Texts to search for
				String[] searchTexts = { "Amphenol-CIT", "Amphenol-CMT",

				};

				// Check if any of the specified texts are present in the page source
				boolean textFound = false;
				for (String searchText : searchTexts) {
					if (pageSource.contains(searchText)) {
						textFound = true;
						break; // Break early if any text is found
					}
				}

				// Only add the page if a matching text was found
				if (textFound) {
					foundPages.add(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Closing the WebDriver when done
			//

			driver.close();
		}

		// Write found page URLs to a text file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("found_pages.txt"))) {
			for (String url : foundPages) {
				writer.write(url);
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
      //-----------------------------------------
        
        
        
        /*
        List<String> urls = readUrlsFromFile("D:\\Ashoksinh Data\\My Desktop\\Felins\\Two files\\Live Pages.txt", "D:\\Ashoksinh Data\\My Desktop\\Felins\\Two files\\Stage.txt");

        // Create text files for storing results
        String matchFilePath = "image_match_results.txt";
        String mismatchFilePath = "image_mismatch_results.txt";
        String unmatchedAltFilePath = "unmatched_alt_images.txt";

        try {
            BufferedWriter matchWriter = new BufferedWriter(new FileWriter(matchFilePath));
            BufferedWriter mismatchWriter = new BufferedWriter(new FileWriter(mismatchFilePath));
            BufferedWriter unmatchedAltTagsWriter = new BufferedWriter(new FileWriter(unmatchedAltFilePath));

            // Loop through each pair of Live and Stage URLs
            for (int i = 0; i < urls.size(); i += 2) {
                String liveUrl = urls.get(i);
                String stageUrl = urls.get(i + 1);

                // Perform comparison for the current pair of URLs
                compareAltTags(driver, liveUrl, stageUrl, matchWriter, mismatchWriter, unmatchedAltTagsWriter);
            }

            matchWriter.close();
            mismatchWriter.close();
            unmatchedAltTagsWriter.close();

            System.out.println("Comparison completed. Results saved in files: " + matchFilePath + ", " + mismatchFilePath + " and " + unmatchedAltFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }

       // driver.quit();
    }

    private static List<String> readUrlsFromFile(String liveFilePath, String stageFilePath) {
        List<String> urls = new ArrayList<>();
        try {
            BufferedReader liveReader = new BufferedReader(new FileReader(liveFilePath));
            BufferedReader stageReader = new BufferedReader(new FileReader(stageFilePath));
            String liveLine;
            String stageLine;
            while ((liveLine = liveReader.readLine()) != null && (stageLine = stageReader.readLine()) != null) {
                urls.add(liveLine);
                urls.add(stageLine);
            }
            liveReader.close();
            stageReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urls;
    }

    private static void compareAltTags(WebDriver driver, String liveUrl, String stageUrl, BufferedWriter matchWriter, BufferedWriter mismatchWriter, BufferedWriter unmatchedAltTagsWriter) {
        try {
            driver.get(liveUrl);
            List<WebElement> liveImages = driver.findElements(By.tagName("img"));

            driver.get(stageUrl);
            List<WebElement> stageImages = driver.findElements(By.tagName("img"));

            if (liveImages.size() != stageImages.size()) {
                mismatchWriter.write("Number of images on Live and Stage pages is different for URLs: " + liveUrl + " and " + stageUrl + "\n\n");
                return;
            }

            boolean allMatch = true;
            // Compare alt tags
            for (int i = 0; i < liveImages.size(); i++) {
                String liveAlt = liveImages.get(i).getAttribute("alt");
                String stageAlt = stageImages.get(i).getAttribute("alt");

                if (!liveAlt.equals(stageAlt)) {
                    allMatch = false;
                    String liveSrc = liveImages.get(i).getAttribute("src");
                    String stageSrc = stageImages.get(i).getAttribute("src");
                    mismatchWriter.write("Alt tag for Image " + (i + 1) + " (src: " + liveSrc + ") on URLs: " + liveUrl + " and " + stageUrl + " does not match.\n");
                    mismatchWriter.write("Live Alt: " + liveAlt + "\n");
                    mismatchWriter.write("Stage Alt: " + stageAlt + "\n\n");
                    
                    // Write details of images with unmatched alt tags to a separate file
                    unmatchedAltTagsWriter.write("Image " + (i + 1) + " on URLs: " + liveUrl + " and " + stageUrl + " has unmatched alt tags.\n");
                    unmatchedAltTagsWriter.write("Live Alt: " + liveAlt + "\n");
                    unmatchedAltTagsWriter.write("Stage Alt: " + stageAlt + "\n");
                    unmatchedAltTagsWriter.write("Image Source (Live): " + liveSrc + "\n");
                    unmatchedAltTagsWriter.write("Image Source (Stage): " + stageSrc + "\n\n");
                }
            }

            if (!allMatch) {
                // Write a message indicating that not all alt tags matched
                mismatchWriter.write("Not all alt tags on images of URLs: " + liveUrl + " and " + stageUrl + " match.\n\n");
            } else {
                // Write a message indicating that all alt tags matched
                matchWriter.write("All alt tags on images of URLs: " + liveUrl + " and " + stageUrl + " match.\n\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
        
        
        */
        //-----------------------------------------
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        /*
        List<String> urls = readUrlsFromFile("D:\\Ashoksinh Data\\My Desktop\\Felins\\Two files\\Live Pages.txt", "D:\\Ashoksinh Data\\My Desktop\\Felins\\Two files\\Stage.txt");

        // Create two text files for storing results
        String matchFilePath = "image_match_results.txt";
        String mismatchFilePath = "image_mismatch_results.txt";

        try {
            BufferedWriter matchWriter = new BufferedWriter(new FileWriter(matchFilePath));
            BufferedWriter mismatchWriter = new BufferedWriter(new FileWriter(mismatchFilePath));

            // Loop through each pair of Live and Stage URLs
            for (int i = 0; i < urls.size(); i += 2) {
                String liveUrl = urls.get(i);
                String stageUrl = urls.get(i + 1);

                // Perform comparison for the current pair of URLs
                compareUrls(driver, liveUrl, stageUrl, matchWriter, mismatchWriter);
            }

            matchWriter.close();
            mismatchWriter.close();

            System.out.println("Comparison completed. Results saved in files: " + matchFilePath + " and " + mismatchFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }

       // driver.quit();
    }

    private static List<String> readUrlsFromFile(String liveFilePath, String stageFilePath) {
        List<String> urls = new ArrayList<>();
        try {
            BufferedReader liveReader = new BufferedReader(new FileReader(liveFilePath));
            BufferedReader stageReader = new BufferedReader(new FileReader(stageFilePath));
            String liveLine;
            String stageLine;
            while ((liveLine = liveReader.readLine()) != null && (stageLine = stageReader.readLine()) != null) {
                urls.add(liveLine);
                urls.add(stageLine);
            }
            liveReader.close();
            stageReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urls;
    }

    private static void compareUrls(WebDriver driver, String liveUrl, String stageUrl, BufferedWriter matchWriter, BufferedWriter mismatchWriter) {
        try {
            driver.get(liveUrl);
            List<WebElement> liveImages = driver.findElements(By.tagName("img"));

            driver.get(stageUrl);
            List<WebElement> stageImages = driver.findElements(By.tagName("img"));

            if (liveImages.size() != stageImages.size()) {
                mismatchWriter.write("Number of images on Live and Stage pages is different for URLs: " + liveUrl + " and " + stageUrl + "\n\n");
                return;
            }

            // Compare alt tags and image titles
            for (int i = 0; i < liveImages.size(); i++) {
                String liveAlt = liveImages.get(i).getAttribute("alt");
                String liveTitle = liveImages.get(i).getAttribute("title");

                String stageAlt = stageImages.get(i).getAttribute("alt");
                String stageTitle = stageImages.get(i).getAttribute("title");

                if (!liveAlt.equals(stageAlt) || !liveTitle.equals(stageTitle)) {
                    mismatchWriter.write("Image " + (i + 1) + " on URLs: " + liveUrl + " and " + stageUrl + " does not match.\n");
                    mismatchWriter.write("Live Alt: " + liveAlt + ", Live Title: " + liveTitle + "\n");
                    mismatchWriter.write("Stage Alt: " + stageAlt + ", Stage Title: " + stageTitle + "\n\n");
                }
            }

            matchWriter.write("All images on URLs: " + liveUrl + " and " + stageUrl + " match.\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
        
        */