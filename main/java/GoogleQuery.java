import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Locale;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class GoogleQuery {
    public String searchKeyword;
    public String url;
    public String content;
    
    // Constructor modified to include food-related keywords and location
    public GoogleQuery(String searchKeyword, String city) throws UnsupportedEncodingException {
        this.searchKeyword = searchKeyword + " restaurant food dining " + city; // Add more food-related context
        this.url = "http://www.google.com/search?q=" + URLEncoder.encode(this.searchKeyword, "UTF-8") + "&oe=utf8&num=20";
    }
    
    // Fetch content from the URL
    private String fetchContent() throws IOException {
        StringBuilder retVal = new StringBuilder();
        URL u = new URL(url);
        URLConnection conn = u.openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.5304.107 Safari/537.36");
        InputStream in = conn.getInputStream();
        InputStreamReader inReader = new InputStreamReader(in, "utf-8");
        BufferedReader bufReader = new BufferedReader(inReader);
        String line;
        while ((line = bufReader.readLine()) != null) {
            retVal.append(line);
        }
        return retVal.toString();
    }
    
    // Query the search engine and parse results with Jsoup
    public HashMap<String, String> query() throws IOException {
        if (content == null) {
            content = fetchContent();
        }
        HashMap<String, String> retVal = new HashMap<>();
        Document doc = Jsoup.parse(content);
        Elements lis = doc.select("div").select(".kCrYT");
        for (Element li : lis) {
            try {
                Element linkElement = li.select("a").first();
                if (linkElement != null) {
                    String citeUrl = linkElement.attr("href");
                    String title = linkElement.select(".vvjwJb").text();
                    if (!title.equals("") && title.toLowerCase(Locale.ROOT).contains("restaurant")) {
                        retVal.put(title, citeUrl);
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                // Log exception, if needed
            }
        }
        return retVal;
    }
}
