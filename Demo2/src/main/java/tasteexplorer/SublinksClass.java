package tasteexplorer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class SublinksClass {

    public List<String> extractSublinks(String url, List<String> userPreferences) {
        Set<String> sublinks = new HashSet<>(); // Using a Set to avoid duplicate URLs
        try {
            // Ensure that the URL has the correct protocol (e.g., "http://" or "https://")
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + url; // You can choose either "http://" or "https://"
            }

            Document document = Jsoup.connect(url)
                    .timeout(1 * 1000) // 10 seconds for both connection and read timeouts
                    .get();
            Elements links = document.select("a[href]");

            for (Element link : links) {
                String sublink = link.attr("abs:href");
                if (matchesUserPreferences(sublink, userPreferences)) {
                    sublinks.add(sublink);
                }
            }
        } catch (IOException e) {
            // Ignore the exception silently, without logging
            // Consider more sophisticated error handling or retry mechanisms here
        }
        return new ArrayList<>(sublinks); // Convert set to list before returning
    }

    private boolean matchesUserPreferences(String url, List<String> userPreferences) {
        // Improved matching using regular expressions
        for (String preference : userPreferences) {
            String regex = ".*\\b" + Pattern.quote(preference.toLowerCase()) + "\\b.*";
            if (Pattern.matches(regex, url.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
