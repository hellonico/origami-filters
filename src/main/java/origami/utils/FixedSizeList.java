package origami.utils;

import java.util.ArrayList;
import java.util.List;

public class FixedSizeList<T> {
    private int maxSize;
    private List<T> list;

    public FixedSizeList(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("Max size must be greater than zero.");
        }
        this.maxSize = maxSize;
        this.list = new ArrayList<>();
    }

    public void add(T element) {
        if (list.size() >= maxSize) {
            list.remove(0); // Remove the oldest element
        }
        list.add(element); // Add the element as the most recent
    }

    public T get(int index) {
        return list.get(index);
    }

    public int size() {
        return list.size();
    }
}