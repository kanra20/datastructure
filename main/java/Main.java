package com.tasteexplorer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

// Import the WebPage class from the correct package
import com.tasteexplorer.WebPage; 
import com.tasteexplorer.CombinedSearch;
import com.tasteexplorer.SearchEngine;
import com.tasteexplorer.SearchServlet;

public class Main {
    public static void main(String[] args) throws IOException {
        // Create an instance of CombinedSearch
        CombinedSearch combinedSearch = new CombinedSearch();

        // Get user input for city and cuisine type
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter city: ");
        String city = scanner.nextLine();
        System.out.print("Enter cuisine type: ");
        String cuisineType = scanner.nextLine();

        // Perform combined search
        List<WebPage> combinedResults = combinedSearch.search(city, cuisineType, Arrays.asList("food", "restaurants"));

        // Display results
        System.out.println("Combined Search Results:");
        displayResults(combinedResults);

        scanner.close();
    }

    private static void displayResults(List<WebPage> results) {
        for (WebPage result : results) {
            System.out.println(result.getTitle() + " - " + result.getUrl() + " (Ranking: " + result.getRanking() + ")");
        }
    }
}