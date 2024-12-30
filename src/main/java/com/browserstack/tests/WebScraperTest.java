package com.browserstack.tests;

import com.browserstack.utils.WebScraper; // Make sure to import WebScraper
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.annotations.AfterClass;

public class WebScraperTest {

    private WebDriver driver;
    private WebScraper webScraper; // Declare the WebScraper instance

    @BeforeClass
    public void setup() {
        // Automatically setup ChromeDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();

        // Set ChromeOptions to resolve WebSocket connection issues
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*"); // Handle cross-origin WebSocket issues
        System.out.println("WebDriverManager cache location: " + System.getProperty("user.home"));

        // Initialize WebDriver with ChromeOptions
        driver = new ChromeDriver(options);
        
        // Initialize the WebScraper instance
        webScraper = new WebScraper();
    }

    @Test
    public void testScrapeArticles() {
        // Call the method to scrape articles from the El País website
        webScraper.scrapArticleFromElPais(driver);
        
    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();  // Close the browser after the test
        }
    }
}
