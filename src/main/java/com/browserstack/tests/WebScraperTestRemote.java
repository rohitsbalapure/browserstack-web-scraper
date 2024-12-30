package com.browserstack.tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.browserstack.utils.BrowserStackSetup;
import com.browserstack.utils.WebScraper;

import org.testng.annotations.AfterClass;

public class WebScraperTestRemote {

    private WebDriver driver;
    private WebScraper webScraper;
    private BrowserStackSetup browserStackSetup;

    @BeforeClass
    public void setup() throws Exception {
        browserStackSetup = new BrowserStackSetup();
        driver = browserStackSetup.setupBrowserStack();  // Setup BrowserStack WebDriver

        webScraper = new WebScraper();  // Initialize WebScraper instance
    }

    @Test
    public void testScrapeArticles() {
        webScraper.scrapArticleFromElPais(driver);  // Call scraping method
    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();  // Close browser after test
        }
    }
}