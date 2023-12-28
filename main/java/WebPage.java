import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class WebPage {
    private String url;
    private String name;
    private WordCounter wordCounter;
    private double score;

    public WebPage(String url, String name) {
        this.url = url;
        this.name = name;
        this.wordCounter = new WordCounter(url); // Assumes that WordCounter is already defined
        this.score = 0.0; // Initialize the score to 0.0
    }

    public void computeScore(List<Keyword> keywords) throws IOException {
        score = 0.0;
        for (Keyword keyword : keywords) {
            int count = wordCounter.countKeyword(keyword.getName());
            score += count * keyword.getWeight();
        }
    }

    // Getters and setters
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getScore() {
        return score;
    }

    // You may want to add additional functionality or helper methods as needed
}
