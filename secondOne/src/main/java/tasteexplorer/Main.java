package tasteexplorer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static SimpleCache urlContentCache = new SimpleCache();

    public static void main(String[] args) throws IOException {
        // Initialize classes
        SearchClass searchClass = new SearchClass();
        SublinksClass sublinksClass = new SublinksClass();
        TreesClass treesClass = new TreesClass();
        KeywordCountClass keywordCountClass = new KeywordCountClass();
        PriorityQueueClass priorityQueue = new PriorityQueueClass();

        // User input for search query using Scanner with UTF-8 encoding
        try (Scanner scanner = new Scanner(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            System.out.print("City: ");
            String city = scanner.nextLine();
            System.out.print("Cuisine type: ");
            String cuisineType = scanner.nextLine();

            // Inbuilt keywords and their weights
            List<String> inbuiltKeywords = Arrays.asList(
                    "food", "restaurant", "main course", "dessert", "meal", "flavor", "drink", "cuisine", "beverages",
                    "eatery", "diner", "café", "bistro", "brasserie", "pub", "canteen", "joint", "establishment"
            );
            List<String> userPreferences = Arrays.asList(city, cuisineType); // Example user preferences

            // Perform search
            List<Entry<String, Integer>> searchResults = searchClass.performSearch(city, cuisineType, userPreferences);

            // Process each search result in parallel
            processSearchResults(searchResults, sublinksClass, treesClass, keywordCountClass, priorityQueue, userPreferences, inbuiltKeywords);

            // Display results in order of priority
            System.out.println("Priority Sorted Search Results:");
            while (!priorityQueue.isEmpty()) {
                Entry<String, Integer> entry = priorityQueue.getNextPage();
                System.out.println(entry.getKey() + " (Score: " + entry.getValue() + ")");
            }
        }
    }




    private static void processSearchResults(List<Entry<String, Integer>> searchResults,
            SublinksClass sublinksClass,
            TreesClass treesClass,
            KeywordCountClass keywordCountClass,
            PriorityQueueClass priorityQueue,
            List<String> userPreferences,
            List<String> inbuiltKeywords) {
ExecutorService executor = Executors.newFixedThreadPool(10); // Thread pool size

for (Entry<String, Integer> entry : searchResults) {
executor.submit(() -> {
String url = entry.getKey();
List<String> sublinks = sublinksClass.extractSublinks(url, userPreferences);

// Filter out irrelevant sublinks early based on the presence of inbuiltKeywords
sublinks.removeIf(sublink -> !containsKeyword(sublink, inbuiltKeywords));

for (String sublink : sublinks) {
String htmlContent = fetchHtmlContent(sublink);
TreesClass.Tree tree = treesClass.buildTreeFromHtml(htmlContent);
int score = keywordCountClass.calculateScore(tree, userPreferences, inbuiltKeywords);
Entry<String, Integer> updatedEntry = new AbstractMap.SimpleEntry<>(url, score);
priorityQueue.addWebPage(updatedEntry);
}
});
}

executor.shutdown();
try {
executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
} catch (InterruptedException e) {
System.err.println("Interrupted while waiting for tasks to finish: " + e.getMessage());
}
}

private static boolean containsKeyword(String content, List<String> keywords) {
// Implement logic to check if content contains any of the specified keywords
// You might need to modify this based on the structure of your content
for (String keyword : keywords) {
if (content.contains(keyword)) {
return true;
}
}
return false;
}


    private static String fetchHtmlContent(String url) {
        if (!isStandardWebPageUrl(url)) {
            return "";
        }

        if (urlContentCache.containsKey(url)) {
            return urlContentCache.get(url);
        }

        try {
            URL urlObject = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(30 * 1000); // Increased timeout
            connection.setReadTimeout(30 * 1000);
            connection.setInstanceFollowRedirects(true); // Follow redirects

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readResponseContent(connection, url);
            } else if (responseCode == HttpURLConnection.HTTP_MOVED_PERM ||
                    responseCode == HttpURLConnection.HTTP_MOVED_TEMP ||
                    responseCode == HttpURLConnection.HTTP_SEE_OTHER) {
                // Handle redirect manually if needed
                String newUrl = connection.getHeaderField("Location");
                return fetchHtmlContent(newUrl); // Recursive call with the new URL
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String readResponseContent(HttpURLConnection connection, String url) throws IOException {
        StringBuilder content = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        urlContentCache.put(url, content.toString());
        return content.toString();
    }


    private static boolean isStandardWebPageUrl(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }
}

class SimpleCache {
    private final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

    public void put(String key, String value) {
        cache.put(key, value);
    }

    public String get(String key) {
        return cache.get(key);
    }

    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }
}
