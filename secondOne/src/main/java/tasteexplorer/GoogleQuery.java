package tasteexplorer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GoogleQuery {
    private String searchKeyword;
    private String url;
    private String content;

    public GoogleQuery(String searchKeyword) {
        this.searchKeyword = searchKeyword;
        this.url = "http://www.google.com/search?q=" + searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
        this.url = "http://www.google.com/search?q=" + searchKeyword;
    }

    public String getUrl() {
        return url;
    }

    public String fetchContent() throws IOException {
        if (url == null) {
            throw new IllegalStateException("URL is not set. Call setSearchKeyword first.");
        }

        if (content != null) {
            return content;
        }

        URL urlObject = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(30 * 1000); // Increased timeout
        connection.setReadTimeout(30 * 1000);
        connection.setInstanceFollowRedirects(true); // Follow redirects

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            content = readResponseContent(connection);
            return content;
        } else {
            throw new IOException("HTTP response code: " + responseCode);
        }
    }

    public List<String> getSearchResults() throws IOException {
        List<String> results = new ArrayList<>();

        // Fetch the content
        String fetchedContent = fetchContent();

        // Implement logic to extract search results from the fetched content
        // For simplicity, let's assume the results are URLs (modify as per actual structure)
        // You may need to use a library like Jsoup for more complex HTML parsing
        // Here, we'll split the content by spaces as a simple example
        String[] words = fetchedContent.split("\\s+");
        for (String word : words) {
            if (word.startsWith("http://") || word.startsWith("https://")) {
                results.add(word);
            }
        }

        return results;
    }

    private String readResponseContent(HttpURLConnection connection) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        }
        return content.toString();
    }

    // Example usage:
    public static void main(String[] args) {
        GoogleQuery googleQuery = new GoogleQuery("example");
        try {
            List<String> searchResults = googleQuery.getSearchResults();
            System.out.println("Search Results:");
            for (String result : searchResults) {
                System.out.println(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
