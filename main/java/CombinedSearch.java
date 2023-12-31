package com.tasteexplorer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

//Import the WebPage class from the correct package
import com.tasteexplorer.WebPage; 
import com.tasteexplorer.Main;
import com.tasteexplorer.SearchEngine;
import com.tasteexplorer.SearchServlet;

public class CombinedSearch implements SearchEngine {
    private static final String GOOGLE_API_KEY = "AIzaSyAPVDwC8y-dWPq3mmWvnsAOkFCb7iGt7s0";
    private static final String GOOGLE_CX = "e514e93aa95bd4b92";
    private static final String DUCKDUCKGO_SEARCH_BASE_URL = "https://duckduckgo.com/html?q=%s";

    @Override
    public List<WebPage> search(String city, String cuisineType, List<String> additionalKeywords) {
        List<WebPage> results = new ArrayList<>();

        // Google Search
        results.addAll(searchGoogle(city, cuisineType, additionalKeywords));

        // DuckDuckGo Search
        results.addAll(searchDuckDuckGo(city, cuisineType, additionalKeywords));

        // Sort the combined results based on ranking and score (descending order)
        results.sort(Comparator.comparingInt(WebPage::getRanking).reversed().thenComparingInt(WebPage::getScore).reversed());

        return results;
    }

    private List<WebPage> searchGoogle(String city, String cuisineType, List<String> additionalKeywords) {
        List<WebPage> results = new ArrayList<>();

        try {
            List<String> keywords = new ArrayList<>();
            keywords.add(city);
            keywords.add(cuisineType);
            keywords.addAll(additionalKeywords);

            String query = String.join(" ", keywords);
            String url = "https://www.googleapis.com/customsearch/v1?key=" + GOOGLE_API_KEY + "&cx=" + GOOGLE_CX + "&q=" + query;
            String content = Jsoup.connect(url).ignoreContentType(true).execute().body();

            results.addAll(parseGoogleResults(content, query));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    private List<WebPage> parseGoogleResults(String content, String query) {
        List<WebPage> actualUrls = new ArrayList<>();

        Document document = Jsoup.parse(content);
        Elements items = document.select("items");

        for (Element item : items) {
            String title = item.select("title").text();
            String link = item.select("link").text();
            int ranking = countKeywordInUrl(link, query);
            int score = calculateScore(Arrays.asList("location"), link);
            actualUrls.add(new WebPage(title, link, ranking, score));
        }

        return actualUrls;
    }

    private List<WebPage> searchDuckDuckGo(String city, String cuisineType, List<String> additionalKeywords) {
        List<WebPage> webPages = new ArrayList<>();

        try {
            String query = city + " " + cuisineType + " " + String.join(" ", additionalKeywords);
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());

            String searchUrl = String.format(DUCKDUCKGO_SEARCH_BASE_URL, encodedQuery);
            String htmlContent = fetchDuckDuckGoResults(searchUrl);

            webPages.addAll(parseDuckDuckGoResults(htmlContent, query));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return webPages;
    }

    private String fetchDuckDuckGoResults(String searchUrl) {
        try {
            Document document = Jsoup.connect(searchUrl).get();
            return document.html();
        } catch (IOException e) {
            e.printStackTrace();
            return "";  // Or handle the exception appropriately
        }
    }

    private List<WebPage> parseDuckDuckGoResults(String htmlContent, String query) {
        List<WebPage> actualUrls = new ArrayList<>();
        HashSet<String> visitedUrls = new HashSet<>();

        Document document = Jsoup.parse(htmlContent);
        Elements links = document.select("a[href]");

        for (Element link : links) {
            String url = link.attr("href");
            // Check if the URL is valid and not visited before
            if (isValidUrl(url) && visitedUrls.add(url)) {
                int ranking = countKeywordInUrl(url, query);
                int score = calculateScore(Arrays.asList("location"), url);
                actualUrls.add(new WebPage(getTitleFromLink(link), url, ranking, score));
            }
        }

        return actualUrls;
    }

    private boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    private String getTitleFromLink(Element link) {
        // Extract title from the link element
        return link.text();
    }

    private int countKeywordInUrl(String url, String keyword) {
        // Use case-insensitive matching for simplicity
        url = url.toLowerCase();
        keyword = keyword.toLowerCase();

        // Split the URL into words and count occurrences
        String[] words = url.split("\\W+"); // Split using non-word characters
        int count = 0;

        for (String word : words) {
            if (word.equals(keyword)) {
                count++;
            }
        }

        return count;
    }

    private int calculateScore(List<String> keywords, String url) {
        int score = 0;

        for (String keyword : keywords) {
            int weight = getKeywordWeight(keyword);
            int keywordCount = countKeywordInUrl(url, keyword);
            score += weight * keywordCount;
        }

        return score;
    }

    private int getKeywordWeight(String keyword) {
        // Assign weights to keywords
        switch (keyword.toLowerCase()) {
            case "location":
                return 5;
            case "food":
                return 5;
            case "restaurant":
                return 4;
            case "main":
            case "course":
                return 2;
            case "dessert":
                return 2;
            case "meal":
                return 3;
            case "flavor":
                return 2;
            case "drink":
                return 3;
            case "cuisine":
                return 3;
            case "beverages":
                return 3;
            default:
                return 1; // Default weight for other keywords
        }
    }
}