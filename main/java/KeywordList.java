import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class KeywordList {
    private ArrayList<Keyword> lst;

    public KeywordList() {
        this.lst = new ArrayList<>();
        // Initialize with in-built keywords and their weights
        // The weight values are illustrative. Adjust them according to your project's specifics.
        lst.add(new Keyword("food", 5));
        lst.add(new Keyword("restaurant", 4));
        lst.add(new Keyword("main course", 2));
        lst.add(new Keyword("dessert", 2));
        lst.add(new Keyword("meal", 3));
        lst.add(new Keyword("flavour", 2));
        lst.add(new Keyword("drink", 3));
        lst.add(new Keyword("cuisine", 3));
        lst.add(new Keyword("beverages", 3));
    }
    
    public void add(Keyword keyword) {
        // Adding a keyword to the list without sorting
        lst.add(keyword);
    }

    public void addUserKeyword(String name) {
        // User keywords have a fixed higher weight of 5
        lst.add(new Keyword(name, 5));
        // Optionally sort the list after adding a new keyword
        sort();
    }
    
    public void sort() {
        // Sorting the list of keywords based on their weight in descending order
        Collections.sort(lst, new Comparator<Keyword>() {
            @Override
            public int compare(Keyword k1, Keyword k2) {
                return Double.compare(k2.getWeight(), k1.getWeight());
            }
        });
    }

    public ArrayList<Keyword> getKeywords() {
        return lst;
    }

    public void printKeywords() {
        for (Keyword keyword : lst) {
            System.out.println(keyword);
        }
    }
}