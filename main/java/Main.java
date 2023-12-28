import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Initialize the keyword list with built-in keywords
        KeywordList keywordList = new KeywordList();
        initializeKeywordList(keywordList);

        // Get user input for search
        System.out.println("Please enter your search query and city:");
        String searchQuery = scanner.nextLine();
        String city = scanner.nextLine();

        // Perform the Google search using GoogleQuery
        GoogleQuery googleQuery = new GoogleQuery(searchQuery, city);
        HashMap<String, String> searchResults = googleQuery.query();

        // Process the results into web nodes and calculate scores
        ArrayList<WebNode> webNodes = processSearchResults(searchResults, keywordList);

        // Sort the web nodes
        webNodes.sort((node1, node2) -> Double.compare(node2.getNodeScore(), node1.getNodeScore()));

        // Output the sorted results
        System.out.println("Sorted Results:");
        for (WebNode webNode : webNodes) {
            WebPage webPage = webNode.getWebPage();
            System.out.println(webPage.getName() + " - Score: " + webNode.getNodeScore());
        }

        scanner.close();
    }

    private static void initializeKeywordList(KeywordList keywordList) {
        // Add built-in keywords to the keyword list
        // Add user-inputted keywords with the highest weight if applicable
    }

    private static ArrayList<WebNode> processSearchResults(HashMap<String, String> searchResults, KeywordList keywordList) throws IOException {
        ArrayList<WebNode> webNodes = new ArrayList<>();
        for (String title : searchResults.keySet()) {
            String url = searchResults.get(title);
            WebPage webPage = new WebPage(url, title);
            WebNode webNode = new WebNode(webPage);
            webNode.computeNodeScore(keywordList.getKeywords()); // Calculate the score for the node
            webNodes.add(webNode);
        }
        return webNodes;
    }
}
