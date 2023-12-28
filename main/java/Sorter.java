import java.util.ArrayList;
import java.util.List;

public class Sorter {
    private List<WebPage> pages;

    public Sorter() {
        this.pages = new ArrayList<>();
    }

    // Add a web page to the list for sorting
    public void addWebPage(WebPage page) {
        pages.add(page);
    }

    // Sort the web pages in descending order of scores
    public void sort() {
        quickSort(0, pages.size() - 1);
    }

    // Perform QuickSort algorithm
    private void quickSort(int leftBound, int rightBound) {
        if (leftBound < rightBound) {
            int pivotIndex = partition(leftBound, rightBound);
            quickSort(leftBound, pivotIndex - 1);
            quickSort(pivotIndex + 1, rightBound);
        }
    }

    // Partition the list based on scores
    private int partition(int leftBound, int rightBound) {
        WebPage pivot = pages.get(rightBound);
        int i = leftBound - 1;

        for (int j = leftBound; j < rightBound; j++) {
            if (pages.get(j).getScore() >= pivot.getScore()) {
                i++;
                swap(i, j);
            }
        }

        swap(i + 1, rightBound);
        return i + 1;
    }

    // Swap two elements in the list
    private void swap(int index1, int index2) {
        WebPage temp = pages.get(index1);
        pages.set(index1, pages.get(index2));
        pages.set(index2, temp);
    }

    // Display the sorted web pages with their scores
    public void output() {
        for (WebPage page : pages) {
            System.out.println(page.getName() + " - Score: " + page.getScore());
        }
    }
}
