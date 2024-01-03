package tasteexplorer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class SearchClass {
    private static final String GOOGLE_API_KEY = "AIzaSyAPVDwC8y-dWPq3mmWvnsAOkFCb7iGt7s0";
    private static final String GOOGLE_CX = "e514e93aa95bd4b92";
    private static final String DUCKDUCKGO_SEARCH_BASE_URL = "https://duckduckgo.com/html?q=%s";

    public List<Map.Entry<String, Integer>> performSearch(String city, String cuisineType, List<String> userPreferences) {
        List<Map.Entry<String, Integer>> results = new ArrayList<>();
        String searchQuery = createSearchQuery(city, cuisineType, userPreferences);

        try {
            // Perform Google search
            List<Map.Entry<String, Integer>> googleResults = performGoogleSearch(searchQuery);
            results.addAll(googleResults);

            // Perform DuckDuckGo search
            List<Map.Entry<String, Integer>> duckDuckGoResults = performDuckDuckGoSearch(searchQuery);
            results.addAll(duckDuckGoResults);
        } catch (IOException e) {
            System.err.println("Error with search: " + e.getMessage());
            // Handle API rate limits and other errors gracefully
        }

        return results;
    }

    private String createSearchQuery(String city, String cuisineType, List<String> userPreferences) {
        // Combine city, cuisineType, and user preferences into a single search query
        String combinedPreferences = String.join(" ", userPreferences);
        return city + " " + cuisineType + " " + combinedPreferences;
    }

    private List<Map.Entry<String, Integer>> performGoogleSearch(String query) throws IOException {
        // Perform a Google search and return results
        List<Map.Entry<String, Integer>> results = new ArrayList<>();

        // Construct the Google search URL
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String googleSearchUrl = "https://www.googleapis.com/customsearch/v1?key=" + GOOGLE_API_KEY +
                "&cx=" + GOOGLE_CX +
                "&q=" + encodedQuery;

        // Make an HTTP request to the Google API and parse the results
        Document googleSearchDocument = Jsoup.connect(googleSearchUrl)
                .ignoreContentType(true) // Ignore content type to bypass the error
                .header("Accept", "application/json") // Specify the expected content type
                .get();
        String jsonResult = googleSearchDocument.text();
        
        // Parse the JSON response
        JsonElement root = JsonParser.parseString(jsonResult);
        if (root.isJsonObject()) {
            JsonObject jsonObject = root.getAsJsonObject();
            if (jsonObject.has("items") && jsonObject.get("items").isJsonArray()) {
                JsonArray items = jsonObject.get("items").getAsJsonArray();
                for (JsonElement item : items) {
                    if (item.isJsonObject() && item.getAsJsonObject().has("link")) {
                        String url = item.getAsJsonObject().get("link").getAsString();
                        results.add(new AbstractMap.SimpleEntry<>(url, 0)); // Assign a score of 0 for Google results
                    }
                }
            }
        }

        return results;
    }

    private List<Map.Entry<String, Integer>> performDuckDuckGoSearch(String query) throws IOException {
        // Perform a DuckDuckGo search and return results
        List<Map.Entry<String, Integer>> results = new ArrayList<>();

        // Construct the DuckDuckGo search URL
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String duckDuckGoSearchUrl = String.format(DUCKDUCKGO_SEARCH_BASE_URL, encodedQuery);

        try {
            // Check if the URL is well-formed
            new URL(duckDuckGoSearchUrl);
            // Make an HTTP request to DuckDuckGo and parse the results
            Document duckDuckGoDocument = Jsoup.connect(duckDuckGoSearchUrl).ignoreContentType(true).get();
            Elements searchResults = duckDuckGoDocument.select(".result__url");

            // Parse the results and add them to the 'results' list
            for (Element result : searchResults) {
                String url = result.text();
                results.add(new AbstractMap.SimpleEntry<>(url, 0)); // Assign a score of 0 for DuckDuckGo results
            }
        } catch (MalformedURLException e) {
            System.err.println("Malformed URL: " + duckDuckGoSearchUrl);
            // Handle malformed URL gracefully
        }

        return results;
    }
}
