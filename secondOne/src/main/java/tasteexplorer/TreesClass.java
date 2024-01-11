package tasteexplorer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.NodeVisitor;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TreesClass {

    public static class Tree {
        String value;
        Tree parent;
        List<Tree> children;

        public Tree(String value) {
            this.value = value;
            this.children = new ArrayList<>();
        }

        public void addChild(Tree child) {
            children.add(child);
            child.parent = this;
        }

        @Override
        public String toString() {
            return toStringHelper("", true);
        }

        private String toStringHelper(String prefix, boolean isTail) {
            StringBuilder builder = new StringBuilder();
            builder.append(prefix).append(isTail ? "└── " : "├── ").append(value).append("\n");
            for (int i = 0; i < children.size() - 1; i++) {
                builder.append(children.get(i).toStringHelper(prefix + (isTail ? "    " : "│   "), false));
            }
            if (children.size() > 0) {
                builder.append(children.get(children.size() - 1).toStringHelper(prefix + (isTail ? "    " : "│   "), true));
            }
            return builder.toString();
        }

        // Enhanced method to analyze the tree for user preference keywords
        public int analyzeForPreferences(List<String> preferenceKeywords) {
            int preferenceMatchCount = 0;
            for (String preference : preferenceKeywords) {
                String regex = ".*\\b" + Pattern.quote(preference.toLowerCase()) + "\\b.*";
                if (Pattern.matches(regex, this.value.toLowerCase())) {
                    preferenceMatchCount++;
                }
            }
            for (Tree child : this.children) {
                preferenceMatchCount += child.analyzeForPreferences(preferenceKeywords);
            }
            return preferenceMatchCount;
        }
    }

    public static Tree buildTreeFromHtml(String htmlContent) {
        Document document = Jsoup.parse(htmlContent);
        Tree root = new Tree("root");
        document.traverse(new NodeVisitor() {
            private Tree current = root;

            @Override
            public void head(Node node, int depth) {
                String nodeValue = node.nodeName().equals("#text") ? node.toString().trim() : node.nodeName();
                if (!nodeValue.isEmpty()) {
                    Tree child = new Tree(nodeValue);
                    current.addChild(child);
                    if (!node.nodeName().equals("#text")) {
                        current = child;
                    }
                }
            }

            @Override
            public void tail(Node node, int depth) {
                if (current.parent != null) {
                    current = current.parent;
                }
            }
        });
        return root;
    }
}
