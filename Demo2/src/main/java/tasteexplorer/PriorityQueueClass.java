package tasteexplorer;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Map;

public class PriorityQueueClass {
    private PriorityQueue<Map.Entry<String, Integer>> queue;

    public PriorityQueueClass() {
        // Use a lambda expression for the comparator
        Comparator<Map.Entry<String, Integer>> comparator = (entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue());
        this.queue = new PriorityQueue<>(comparator);
    }

    public void addWebPage(Map.Entry<String, Integer> entry) {
        queue.add(entry);
    }

    public Map.Entry<String, Integer> getNextPage() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

