package com.browserstack.utils;


import java.io.File;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import okhttp3.*;


public class WebScraper {

    public void scrapArticleFromElPais(WebDriver driver) {
        // Navigate
        driver.get("https://elpais.com/opinion/");
        
        // Wait for the articles to be visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("article")));
        
        List<String> translatedTitles = new ArrayList<>();
        List<WebElement> articles = driver.findElements(By.cssSelector("article h2 a"));
        
        // Scrape the first 5 article titles
        for (int i = 0; i < 5; i++) {
            if (i >= articles.size()) break;  
            
            WebElement article = articles.get(i);
            String title = article.getText();
            
           
            String translatedTitle = translateText(title);
            
            // Print original and translated titles
            System.out.println("Original Title: " + i +" "+ title);
            System.out.println("Translated Title: " + i +" "+ translatedTitle);
       
            translatedTitles.add(i +" "+ translatedTitle);
            analyzeRepeatedWords(translatedTitles);
            downloadArticleImage(article);
        }
    }
    
    private void analyzeRepeatedWords(List<String> translatedTitles) {
        Map<String, Integer> wordCountMap = new HashMap<>();
        
        // Process each translated title using a standard indexed for loop
        for (int i = 0; i < translatedTitles.size(); i++) {
            String title = translatedTitles.get(i);
            String[] words = title.split("\\s+");  // Split the title into words
            
            // Count occurrences of each word using an indexed for loop
            for (int j = 0; j < words.length; j++) {
                String word = words[j].toLowerCase().replaceAll("[^a-zA-Z]", ""); // Normalize word
                if (!word.isEmpty()) {
                    wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
                }
            }
        }
        
        // Print repeated words (more than twice)
        System.out.println("Repeated Words in Translated Titles:");
        for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
            if (entry.getValue() > 2) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }




    public String translateText(String textToTranslate) {
        // Define the API URL and API Key inside the method
        String API_URL = "https://aibit-translator.p.rapidapi.com/api/v1/translator/text";  
        String API_KEY = "82aa2b1fbbmsh4ef73325297a2f5p1bea3bjsnba30f3e7d41a";  // Your actual API key

        OkHttpClient client = new OkHttpClient();

        // Create the JSON body for the API request
        MediaType mediaType = MediaType.parse("application/json");
        String jsonBody = "{\n" +
                          "    \"from\": \"auto\",\n" +  // Auto detects source language
                          "    \"to\": \"en\",\n" +     // Target language is English
                          "    \"text\": \"" + textToTranslate + "\"\n" +
                          "}";

        RequestBody body = RequestBody.create(mediaType, jsonBody);

        // Build the request with the required headers
        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)  // Use POST method
                .addHeader("X-RapidAPI-Key", API_KEY)  // Add API Key for authentication
                .addHeader("X-RapidAPI-Host", "aibit-translator.p.rapidapi.com")  // Correct API host
                .addHeader("Content-Type", "application/json")  // Set the content type to JSON
                .build();

        // Execute the request and get the response
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("Request failed with status code: " + response.code());
                return textToTranslate;  // Return the original text if the API call fails
            }

            // Parse the response to get the translated text
            String responseBody = response.body().string();
            System.out.println("Response: " + responseBody);

            // Extract translated text from the JSON response (use "trans" instead of "translatedText")
            JSONObject myResponse = new JSONObject(responseBody);
            return myResponse.getString("trans");

        } catch (IOException e) {
            System.out.println("Error during translation API call: " + e.getMessage());
            return textToTranslate;  // Return the original text if an error occurs
        }
    }
    

  
    
    private void downloadArticleImage(WebElement article) {
        try {
            // Try to locate the image element
            WebElement imageElement = article.findElement(By.xpath(".//img"));

            // Get the image URL
            String imageUrl = imageElement.getAttribute("src");

            // If image URL is empty or invalid, skip processing
            if (imageUrl == null || imageUrl.isEmpty() || !imageUrl.startsWith("http")) {
                return; // Skip if no valid image URL found
            }

            System.out.println("Processing image URL: " + imageUrl);

            // Try downloading the image (no errors thrown)
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000); // Timeout for connection
            connection.setReadTimeout(5000); // Timeout for reading

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                return; // Skip if the URL is not reachable
            }

            // Ensure the directory exists
            File directory = new File("images/");
            if (!directory.exists()) {
                directory.mkdir(); // Create directory if it doesn't exist
            }

            // Save the image to the 'images' directory
            File file = new File(directory, "article_image_" + System.currentTimeMillis() + ".jpg");
            try (InputStream inputStream = connection.getInputStream();
                 FileOutputStream fileOutputStream = new FileOutputStream(file)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }

                System.out.println("Image saved: " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            // Catch any errors and just continue without throwing exceptions
            // No error will be thrown, and we just return
        }
    }


    
}
