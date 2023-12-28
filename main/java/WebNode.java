import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebNode {
    private WebNode parent;
    private List<WebNode> children;
    private WebPage webPage;
    private double nodeScore;

    public WebNode(WebPage webPage) {
        this.webPage = webPage;
        this.children = new ArrayList<>();
        this.nodeScore = 0.0; // Initialize to 0
    }

    public void addChild(WebNode child) {
        this.children.add(child);
        child.setParent(this); // Set the parent of the child
    }

    public void computeNodeScore(ArrayList<Keyword> keywords) throws IOException {
        // Compute the score for the webpage
        webPage.computeScore(keywords);

        // Set this webPage's score as the initial node score
        this.nodeScore = webPage.getScore();

        // Aggregate the score with all children's nodeScore
        for (WebNode child : children) {
            child.computeNodeScore(keywords); // Make sure to compute child scores first
            this.nodeScore += child.nodeScore;
        }
    }

    // Getters and setters
    public WebNode getParent() {
        return parent;
    }

    private void setParent(WebNode parent) {
        this.parent = parent;
    }

    public List<WebNode> getChildren() {
        return children;
    }

    public WebPage getWebPage() {
        return webPage;
    }

    public double getNodeScore() {
        return nodeScore;
    }

    // Utility methods
    public boolean isTheLastChild() {
        return parent == null || this.equals(parent.getChildren().get(parent.getChildren().size() - 1));
    }

    public int getDepth() {
        int depth = 1;
        WebNode current = this;
        while (current.parent != null) {
            depth++;
            current = current.parent;
        }
        return depth;
    }

    // Method to recursively print the tree structure
    public void printTreeStructure() {
        printTreeStructure(this, 0);
    }

    private void printTreeStructure(WebNode node, int depth) {
        // Print current node
        System.out.println("-".repeat(depth) + "> " + node.webPage.getName() + " (Score: " + node.nodeScore + ")");

        // Print children nodes
        for (WebNode child : node.children) {
            printTreeStructure(child, depth + 1);
        }
    }
}
