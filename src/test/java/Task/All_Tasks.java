package Task;

import org.testng.annotations.*;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.ImmutableMap;

import io.appium.java_client.android.AndroidDriver;

public class All_Tasks{
	
	public static AndroidDriver driver;
	Properties props;
	InputStream inputStream;
	WebDriverWait wait;
	

	@BeforeMethod
	public void setUp() throws Exception {
			props =new Properties();
			String propFileName= "config.properties";
			inputStream= getClass().getClassLoader().getResourceAsStream(propFileName);
			props.load(inputStream);
		 
		 	DesiredCapabilities caps = new DesiredCapabilities();
	        caps.setCapability("platformName", props.getProperty("platformName"));
	        caps.setCapability("deviceName", props.getProperty("deviceName"));
	        caps.setCapability("udid", props.getProperty("udid")); //emulator-5554 78ee1e6a
	        caps.setCapability("automationName", props.getProperty("automationName"));
	        caps.setCapability("autoAcceptAlert", true);
	        caps.setCapability("autoGrantPermissions", true);
	        caps.setCapability("adbExecTimeout", 30000);;
	        String appURL= System.getProperty("user.dir")+ 
	        		 File.separator+ "src"+ File.separator+"test"+ File.separator+"resources"+
	        		 File.separator+"app"+ File.separator+"WikipediaSample.apk";
	        caps.setCapability("app", appURL);
	        caps.setCapability("appPackage", props.getProperty("appPackage"));
	        caps.setCapability("appActivity", props.getProperty("appActivity"));
	        //   caps.setCapability("noReset", true);

	        driver = new AndroidDriver(new URL(props.getProperty("appiumURL")), caps);
	        WebElement okBtn= waitForElementVisibility(By.xpath("//android.widget.Button[@resource-id=\"android:id/button1\"]"));
	        okBtn.click(); //  app update pop up ok button click
	}
	
	
	public static WebElement waitForElementVisibility(By by) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }
	

	public void scrollPage(String direction) {
		  boolean isEndReached = false;
		    String lastPageSource = "";
		    int maxRetries = 10; // To prevent infinite loop
		    int retries = 0;

		    while (!isEndReached && retries < maxRetries) {
		        // Get the current page source
		        String currentPageSource = driver.getPageSource();

		        // Check if the page source has not changed (indicates end of the page)
		        if (currentPageSource.equals(lastPageSource)) {
		            isEndReached = true;
		        } else {
		            lastPageSource = currentPageSource; // Update the last known page source

		            // Perform scroll action
		            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		            jsExecutor.executeScript("mobile: scrollGesture", ImmutableMap.of(
		                "left", 100,          // Starting x-coordinate
		                "top", 600,           // Starting y-coordinate
		                "width", 800,         // Width of the scrollable area
		                "height", 1200,       // Height of the scrollable area
		                "direction", direction,  // Direction of the scroll
		                "percent", 1.0        // Percentage of screen height to scroll
		            ));  	
		            
		            		        }
		        retries++;
		    }

	}
	
	@Test(priority=1)
	public void taskOne() throws InterruptedException {
		Thread.sleep(1000);
		scrollPage("down"); // scroll down page
		WebElement myListTab= waitForElementVisibility(By.xpath("//android.widget.FrameLayout[@content-desc=\"My lists\"]"));
		myListTab.click(); // click on My List Tab
        Thread.sleep(3000); // wait for 3 second 
		WebElement historyTab= waitForElementVisibility(By.xpath("//android.widget.FrameLayout[@content-desc=\"History\"]"));
		historyTab.click(); // click on history Tab
	    Thread.sleep(3000);// wait for 3 second
		WebElement nearByTab= waitForElementVisibility(By.xpath("//android.widget.FrameLayout[@content-desc=\"Nearby\"]"));
		nearByTab.click(); // click on nearby Tab
        Thread.sleep(3000);// wait for 3 second
		WebElement homeTab= waitForElementVisibility(By.xpath("//android.widget.FrameLayout[@content-desc=\"Explore\"]"));
		homeTab.click(); // Click on Home Tab
	    Thread.sleep(1000);
	    scrollPage("up");   // Scroll up page     
                
	}
	
	@Test(priority=2)                       
	public void taskTwo() throws InterruptedException { 
		WebElement searchContainer= waitForElementVisibility(By.id("org.wikipedia.alpha:id/search_container"));
		searchContainer.click(); // click on search container
		WebElement searchTextBox= waitForElementVisibility(By.id("org.wikipedia.alpha:id/search_src_text"));
		searchTextBox.sendKeys("New York"); // enter 'New York' in search box.
		Thread.sleep(2000);
		WebElement searchResult= waitForElementVisibility(By.xpath("//android.widget.FrameLayout[@resource-id=\"org.wikipedia.alpha:id/fragment_search_results\"]"));
		assertTrue(searchResult.isDisplayed()); // Asserting result
		WebElement clearBtn= waitForElementVisibility(By.xpath("//android.widget.ImageView[@content-desc=\"Clear query\"]")); 
		
		for(int i=0; i<2; i++) {
			clearBtn.click(); // Click on clear button twice.
		}
	}
	
	
	@Test(priority=3)
	public void taskThree() throws InterruptedException {
		WebElement moreOptionBtn= waitForElementVisibility(By.xpath("//android.widget.TextView[@content-desc=\"More options\"]"));
		moreOptionBtn.click(); // click on more option button
		WebElement settingBtn= waitForElementVisibility(By.id("org.wikipedia.alpha:id/explore_overflow_settings"));
		settingBtn.click(); // click on setting button
		List<WebElement> options=  driver.findElements(By.xpath("//android.widget.Switch[@resource-id=\"org.wikipedia.alpha:id/switchWidget\"]"));
		WebElement backBtn= waitForElementVisibility(By.xpath("//android.widget.ImageButton[@content-desc=\"Navigate up\"]"));
		Thread.sleep(2000);
		
		// Disable all visible options
        for (int i=0; i<options.size(); i++) {
            if (options.get(i).getDomAttribute("checked").equals("true")) { // If switch is ON
            	options.get(i).click(); // Turn it OFF
            	//Thread.sleep(1000);
            }
        }
        backBtn.click(); // click on back button to navigate back to home screen
	}
	
	@AfterMethod
	public void tearDown() {
		driver.quit();
	}
}