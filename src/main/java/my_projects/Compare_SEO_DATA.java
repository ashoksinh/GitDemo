package my_projects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.HashMap;
import java.util.Map;
public class Compare_SEO_DATA {

 	



	
	 public static void main(String[] args) {

		 // Set path to the ChromeDriver executable
			WebDriver driver = new ChromeDriver();
		// System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");

	  
	        // List of URLs to compare
	                		String[] liveUrls = {
	        			    "https://blackbeardiner.com/",
	        			    "https://blackbeardiner.com/founders/",
	        			    "https://blackbeardiner.com/animal-welfare/",
	        			    "https://blackbeardiner.com/giving/",
	        			    "https://blackbeardiner.com/bears-brew-back/",
	        			    "https://blackbeardiner.com/contact-us/",
	        			    "https://blackbeardiner.com/make-a-wish/",
	        			    "https://blackbeardiner.com/black-bear-diner-has-its-sights-on-becoming-a-national-brand/",
	        			    "https://blackbeardiner.com/careers/",
	        			    "https://blackbeardiner.com/privacy-policy/",
	        			    "https://blackbeardiner.com/terms-conditions/",
	        			    "https://blackbeardiner.com/community/",
	        			    "https://blackbeardiner.com/ceo-letter/",
	        			    "https://blackbeardiner.com/official-contest-rules-sneak-peek-contest/",
	        			    "https://blackbeardiner.com/officialcontestrules_blackbeardiner_sms_100_gift_certificate_sweepstakes/",
	        			    "https://blackbeardiner.com/valentines-contest-rules/",
	        			    "https://blackbeardiner.com/passport-program-road-trip-2021-giveaway/",
	        			    "https://blackbeardiner.com/location/fayetteville/",
	        			    "https://blackbeardiner.com/black-bear-diner-in-bakersfield/",
	        			    "https://blackbeardiner.com/locations-coming-soon/",
	        			    "https://blackbeardiner.com/contact-thank-you/",
	        			    "https://blackbeardiner.com/bbd_idfa_sep2018/",
	        			    "https://blackbeardiner.com/health-and-safety-promise/",
	        			    "https://blackbeardiner.com/big-daddy-contest/",
	        			    "https://blackbeardiner.com/food-gallery/",
	        			    "https://blackbeardiner.com/bears-brew-back-trivia-contest/",
	        			    "https://blackbeardiner.com/meeting-rooms/",
	        			    "https://blackbeardiner.com/lto-shrimp/",
	        			    "https://blackbeardiner.com/black-bear-diner-in-california/",
	        			    "https://blackbeardiner.com/black-bear-diner-in-colorado/",
	        			    "https://blackbeardiner.com/black-bear-diner-in-idaho/",
	        			    "https://blackbeardiner.com/black-bear-diner-in-lakewood/",
	        			    "https://blackbeardiner.com/black-bear-diner-in-los-angeles/",
	        			    "https://blackbeardiner.com/black-bear-diner-in-phoenix/",
	        			    "https://blackbeardiner.com/black-bear-diner-near-portland/",
	        			    "https://blackbeardiner.com/black-bear-diner-in-reno-area/",
	        			    "https://blackbeardiner.com/black-bear-diner-in-seattle/",
	        			    "https://blackbeardiner.com/black-bear-diner-in-socal/",
	        			    "https://blackbeardiner.com/black-bear-diner-in-sonoma-ca/",
	        			    "https://blackbeardiner.com/black-bear-diner-in-utah/",
	        			    "https://blackbeardiner.com/black-bear-diner-in-washington/",
	        			    "https://blackbeardiner.com/pancake-coupon/",
	        			    "https://blackbeardiner.com/richey-road-promo-10/",
	        			    "https://blackbeardiner.com/richey-road-promo-25/",
	        			    "https://blackbeardiner.com/holiday-meals/",
	        			    "https://blackbeardiner.com/managers-conference-2023/",
	        			    "https://blackbeardiner.com/lets-hash-this-out/",
	        			    "https://blackbeardiner.com/lto-burger/",
	        			    "https://blackbeardiner.com/lto-benedict/",
	        			    "https://blackbeardiner.com/black-bear-diner-in-las-vegas/",
	        			    "https://blackbeardiner.com/franchise-thank-you/",
	        			    "https://blackbeardiner.com/press-thank-you/",
	        			    "https://blackbeardiner.com/survey-thank-you/",
	        			    "https://blackbeardiner.com/e-club-thank-you/",
	        			    "https://blackbeardiner.com/passport-thank-you/",
	        			    "https://blackbeardiner.com/news-and-awards/",
	        			    "https://blackbeardiner.com/locations/",
	        			    "https://blackbeardiner.com/menus/",
	        			    "https://blackbeardiner.com/applicant-notice/"
	        			};
	     
	        String[] stagingUrls = {
	        	    "https://demo:Wliq@1.2.3@blackbeardine1.wpenginepowered.com/",
	        	    "https://blackbeardine1.wpenginepowered.com/clubs/",
	        	    "https://blackbeardine1.wpenginepowered.com/founders/",
	        	    "https://blackbeardine1.wpenginepowered.com/animal-welfare/",
	        	    "https://blackbeardine1.wpenginepowered.com/giving/",
	        	    "https://blackbeardine1.wpenginepowered.com/bears-brew-back/",
	        	    "https://blackbeardine1.wpenginepowered.com/contact-us/",
	        	    "https://blackbeardine1.wpenginepowered.com/make-a-wish/",
	        	    "https://blackbeardine1.wpenginepowered.com/black-bear-diner-has-its-sights-on-becoming-a-national-brand/",
	        	    "https://blackbeardine1.wpenginepowered.com/careers/",
	        	    "https://blackbeardine1.wpenginepowered.com/privacy-policy/",
	        	    "https://blackbeardine1.wpenginepowered.com/terms-conditions/",
	        	    "https://blackbeardine1.wpenginepowered.com/community/",
	        	    "https://blackbeardine1.wpenginepowered.com/ceo-letter/",
	        	    "https://blackbeardine1.wpenginepowered.com/official-contest-rules-sneak-peek-contest/",
	        	    "https://blackbeardine1.wpenginepowered.com/officialcontestrules_blackbeardiner_sms_100_gift_certificate_sweepstakes/",
	        	    "https://blackbeardine1.wpenginepowered.com/valentines-contest-rules/",
	        	    "https://blackbeardine1.wpenginepowered.com/passport-program-road-trip-2021-giveaway/",
	        	    "https://blackbeardine1.wpenginepowered.com/location/fayetteville/",
	        	    "https://blackbeardine1.wpenginepowered.com/black-bear-diner-in-bakersfield/",
	        	    "https://blackbeardine1.wpenginepowered.com/locations-coming-soon/",
	        	    "https://blackbeardine1.wpenginepowered.com/contact-thank-you/",
	        	    "https://blackbeardine1.wpenginepowered.com/bbd_idfa_sep2018/",
	        	    "https://blackbeardine1.wpenginepowered.com/health-and-safety-promise/",
	        	    "https://blackbeardine1.wpenginepowered.com/big-daddy-contest/",
	        	    "https://blackbeardine1.wpenginepowered.com/food-gallery/",
	        	    "https://blackbeardine1.wpenginepowered.com/bears-brew-back-trivia-contest/",
	        	    "https://blackbeardine1.wpenginepowered.com/meeting-rooms/",
	        	    "https://blackbeardine1.wpenginepowered.com/lto-shrimp/",
	        	    "https://blackbeardine1.wpenginepowered.com/black-bear-diner-in-california/",
	        	    "https://blackbeardine1.wpenginepowered.com/black-bear-diner-in-colorado/",
	        	    "https://blackbeardine1.wpenginepowered.com/black-bear-diner-in-idaho/",
	        	    "https://blackbeardine1.wpenginepowered.com/black-bear-diner-in-lakewood/",
	        	    "https://blackbeardine1.wpenginepowered.com/black-bear-diner-in-los-angeles/",
	        	    "https://blackbeardine1.wpenginepowered.com/black-bear-diner-in-phoenix/",
	        	    "https://blackbeardine1.wpenginepowered.com/black-bear-diner-near-portland/",
	        	    "https://blackbeardine1.wpenginepowered.com/black-bear-diner-in-reno-area/",
	        	    "https://blackbeardine1.wpenginepowered.com/black-bear-diner-in-seattle/",
	        	    "https://blackbeardine1.wpenginepowered.com/black-bear-diner-in-socal/",
	        	    "https://blackbeardine1.wpenginepowered.com/black-bear-diner-in-sonoma-ca/",
	        	    "https://blackbeardine1.wpenginepowered.com/black-bear-diner-in-utah/",
	        	    "https://blackbeardine1.wpenginepowered.com/black-bear-diner-in-washington/",
	        	    "https://blackbeardine1.wpenginepowered.com/pancake-coupon/",
	        	    "https://blackbeardine1.wpenginepowered.com/richey-road-promo-10/",
	        	    "https://blackbeardine1.wpenginepowered.com/richey-road-promo-25/",
	        	    "https://blackbeardine1.wpenginepowered.com/holiday-meals/",
	        	    "https://blackbeardine1.wpenginepowered.com/managers-conference-2023/",
	        	    "https://blackbeardine1.wpenginepowered.com/lets-hash-this-out/",
	        	    "https://blackbeardine1.wpenginepowered.com/lto-burger/",
	        	    "https://blackbeardine1.wpenginepowered.com/lto-benedict/",
	        	    "https://blackbeardine1.wpenginepowered.com/black-bear-diner-in-las-vegas/",
	        	    "https://blackbeardine1.wpenginepowered.com/franchise-thank-you/",
	        	    "https://blackbeardine1.wpenginepowered.com/press-thank-you/",
	        	    "https://blackbeardine1.wpenginepowered.com/survey-thank-you/",
	        	    "https://blackbeardine1.wpenginepowered.com/e-club-thank-you/",
	        	    "https://blackbeardine1.wpenginepowered.com/passport-thank-you/",
	        	    "https://blackbeardine1.wpenginepowered.com/news-and-awards/",
	        	    "https://blackbeardine1.wpenginepowered.com/locations/",
	        	    "https://blackbeardine1.wpenginepowered.com/menus/",
	        	    "https://blackbeardine1.wpenginepowered.com/applicant-notice/"
	        	};
	    
	        // Maps to store data
	        Map<String, String[]> liveSiteData = new HashMap<>();
	        Map<String, String[]> stagingSiteData = new HashMap<>();

	        // Collect data from live site
	        collectData(driver, liveUrls, liveSiteData);

	        // Collect data from staging site
	        collectData(driver, stagingUrls, stagingSiteData);

	        // Compare data
	        compareData(liveSiteData, stagingSiteData);

	        // Close the browser
	        driver.quit();
	    }

	    private static void collectData(WebDriver driver, String[] urls, Map<String, String[]> dataMap) {
	        for (String url : urls) {
	            driver.get(url);

	            String title = driver.getTitle();

	            WebElement metaDescriptionElement = driver.findElement(By.cssSelector("meta[name='description']"));
	            String metaDescription = metaDescriptionElement != null ? metaDescriptionElement.getAttribute("content") : "No meta description";

	            dataMap.put(url, new String[]{title, metaDescription});
	        }
	    }

	    private static void compareData(Map<String, String[]> liveData, Map<String, String[]> stagingData) {
	        for (String url : liveData.keySet()) {
	            String[] liveDataArray = liveData.get(url);
	            String[] stagingDataArray = stagingData.get(url);

	            if (stagingDataArray == null) {
	                System.out.println("URL not found in staging: " + url);
	                continue;
	            }

	            String liveTitle = liveDataArray[0];
	            String liveMetaDescription = liveDataArray[1];
	            String stagingTitle = stagingDataArray[0];
	            String stagingMetaDescription = stagingDataArray[1];

	            if (!liveTitle.equals(stagingTitle)) {
	                System.out.println("Title mismatch for URL: " + url);
	                System.out.println("Live Site Title: " + liveTitle);
	                System.out.println("Staging Site Title: " + stagingTitle);
	            }

	            if (!liveMetaDescription.equals(stagingMetaDescription)) {
	                System.out.println("Meta Description mismatch for URL: " + url);
	                System.out.println("Live Site Meta Description: " + liveMetaDescription);
	                System.out.println("Staging Site Meta Description: " + stagingMetaDescription);
	            }
	        }
	    }
	}