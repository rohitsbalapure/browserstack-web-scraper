package com.browserstack.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.URL;

public class BrowserStackSetup {

    public WebDriver setupBrowserStack() throws Exception {
       
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "Chrome");
        capabilities.setCapability("browserVersion", "latest");
        capabilities.setCapability("platformName", "Windows");

        // BrowserStack credentials
        String username = "rohitbalapure_OYJF4E"; 
        String accessKey = "zvKpAxqBX7rtrvT4bu3U"; 

        // Setup the URL for BrowserStack Remote WebDriver
        URL url = new URL("https://" + username + ":" + accessKey + "@hub-cloud.browserstack.com/wd/hub");

        // Initialize ChromeOptions
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu");

        // Merge the capabilities with ChromeOptions
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);

        // Create RemoteWebDriver instance with merged capabilities
        WebDriver driver = new RemoteWebDriver(url, capabilities);

        return driver;
    }
}
