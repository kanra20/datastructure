package tasteexplorer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeywordCountClass {
    private Map<String, Integer> dynamicWeights;

    public KeywordCountClass() {
        this.dynamicWeights = new HashMap<>();
        initializeDefaultWeights();
    }

    private void initializeDefaultWeights() {
        // Initialize with default weights; these can be adjusted as needed
        dynamicWeights.put("food", 5);
        dynamicWeights.put("restaurant", 5);
        dynamicWeights.put("main course", 3);
        dynamicWeights.put("dessert", 3);
        dynamicWeights.put("meal", 3);
        dynamicWeights.put("flavor", 2);
        dynamicWeights.put("drink", 2);
        dynamicWeights.put("cuisine", 2);
        dynamicWeights.put("beverages", 2);
    }

    public void setDynamicWeights(Map<String, Integer> dynamicWeights) {
        this.dynamicWeights = dynamicWeights;
    }

    public int calculateScore(TreesClass.Tree webpageTree, List<String> userKeywords, List<String> inbuiltKeywords) {
        if (webpageTree == null) {
            return 0;
        }
        return calculateScoreRecursive(webpageTree, userKeywords, inbuiltKeywords);
    }

    private int calculateScoreRecursive(TreesClass.Tree node, List<String> userKeywords, List<String> inbuiltKeywords) {
        if (node == null) {
            return 0;
        }

        int score = 0;
        String keyword = node.value.toLowerCase();
        int keywordWeight = getKeywordWeight(keyword, userKeywords, inbuiltKeywords);
        score += keywordWeight;

        // Recursively calculate the score for each child node
        for (TreesClass.Tree child : node.children) {
            score += calculateScoreRecursive(child, userKeywords, inbuiltKeywords);
        }

        return score;
    }

    private int getKeywordWeight(String keyword, List<String> userKeywords, List<String> inbuiltKeywords) {
        // Assign weights based on the provided score formulation
        if (userKeywords.contains(keyword)) {
            return 5; // Default weight for user-inputted keywords
        } else if (inbuiltKeywords.contains(keyword)) {
            return dynamicWeights.getOrDefault(keyword, 1); // Get weight from dynamicWeights or default to 1
        }
        return 1; // Default weight for other keywords not found
    }
}
