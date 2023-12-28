import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreCalculator {
    private List<WebPage> webPages;
    private KeywordList keywordList;

    public ScoreCalculator(List<WebPage> webPages, KeywordList keywordList) {
        if (webPages == null || keywordList == null) {
            throw new IllegalArgumentException("WebPages and KeywordList must not be null.");
        }
        this.webPages = webPages;
        this.keywordList = keywordList;
    }

    public void calculateScores() {
        for (WebPage page : webPages) {
            double score = 0;
            try {
                Map<String, Integer> keywordOccurrences = countKeywordsInPageContent(page);

                for (Keyword keyword : keywordList.getKeywords()) {
                    Integer occurrences = keywordOccurrences.getOrDefault(keyword.getName(), 0);
                    score += occurrences * getWeightFor(keyword);
                }
                page.setScore(score);
            } catch (IOException e) {
                // Handle the IOException here
                e.printStackTrace();
            }
        }
    }

    private Map<String, Integer> countKeywordsInPageContent(WebPage page) throws IOException {
        Map<String, Integer> occurrences = new HashMap<>();
        for (Keyword keyword : keywordList.getKeywords()) {
            int count = page.getWordCounter().countKeyword(keyword.getName());
            occurrences.put(keyword.getName(), count);
        }
        return occurrences;
    }

    private double getWeightFor(Keyword keyword) {
        // Implement logic to adjust weights if needed
        return keyword.getWeight();
    }
}
